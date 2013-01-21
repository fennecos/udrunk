from django.contrib.auth.models import User
from django.db.models.query import EmptyQuerySet
from django.contrib.auth.models import User

from tastypie import fields
from tastypie.resources import ModelResource
from tastypie.authorization import Authorization, ReadOnlyAuthorization
from tastypie.authentication import ApiKeyAuthentication, Authentication
from tastypie.serializers import Serializer
from tastypie.validation import Validation
from tastypie.bundle import Bundle

from notification.models import Notice

from apps.core import prettydate, epoch
from apps.place.models import Place
from apps.core.models import Profile, Friend, Checkin

''' 
' Default
'''
class BackboneCompatibleResource(ModelResource):
	class Meta:
		always_return_data = True
		authorization = Authorization()
		authentication = ApiKeyAuthentication()
		serializer = Serializer()
		validation = Validation()	
		default_format = "application/json"
		default_format="application/json"


'''
' Resources
'''
class SimpleUserResource(BackboneCompatibleResource):
	class Meta(BackboneCompatibleResource.Meta): 
		queryset = User.objects.all()
		resource_name = 'user'
		excludes = ['is_active', 'is_staff', 'is_superuser', 'password']
		detail_allowed_methods = ['get',]

	def dehydrate(self, bundle):
		result = super(SimpleUserResource, self).dehydrate(bundle)
		result.data['avatar'] = bundle.obj.get_profile().get_avatar()
		result.data['date_joined'] = epoch(bundle.obj.date_joined)
		result.data['last_login'] = epoch(bundle.obj.last_login)
		return result
		
class SubscriptionResource(BackboneCompatibleResource):
	class Meta(BackboneCompatibleResource.Meta): 
		authentication = Authentication()
		resource_name = 'subscribe'
		allowed_methods = ['post',]

class LoginResource(BackboneCompatibleResource):
	class Meta(BackboneCompatibleResource.Meta):
		queryset = User.objects.all()
		resource_name = 'login'
		fields = ['api_key', 'username', 'id']
		filtering = {
			'login':['exact'],
			'pass': ['exact']
		}
	
	def apply_filters(self, request, applicable_filters): 
		base_object_list = super(LoginResource, self).apply_filters(request, applicable_filters) 
		password = request.GET.get('pass', None) 
		login    = request.GET.get('login', None)
		if password and login: 
			base_object_list = base_object_list.filter(username=login)
			for o in base_object_list:
				if o.check_password(password):
					return base_object_list.filter(pk=o.pk)
		return base_object_list.none()
	
	def dehydrate(self, bundle):
		result = super(LoginResource, self).dehydrate(bundle)
		result.data['api_key'] = bundle.obj.api_key.key
		return result
	
	
# TODO 		
#class UserResource(BackboneCompatibleResource):
#	checkins = fields.ToManyField('apps.place.api.CheckinResource', 'checkin_set')
		
class FeedResource(BackboneCompatibleResource):
	user   = fields.ToOneField(SimpleUserResource, 'user', full=True)
	place  = fields.ToOneField('apps.place.api.PlaceResource', 'place', full=True)
	
	class Meta(BackboneCompatibleResource.Meta): 
		queryset = Checkin.objects.all()
		resource_name = 'feed'

	def obj_get_list(self, request):
		friends = User.objects.filter(user1__id=request.user.id) | User.objects.filter(user2__id=request.user.id) | User.objects.filter(pk=request.user.id)
		list    = self.get_object_list(request)
		return list.filter(user__in=friends).order_by('-added')
	
	def dehydrate(self, bundle):
		result = super(FeedResource, self).dehydrate(bundle)
		result.data['added'] = epoch(bundle.obj.added)
		return result
		
class CheckinResource(BackboneCompatibleResource):
	user  = fields.ForeignKey(SimpleUserResource, 'user')
	place = fields.ToOneField('apps.place.api.PlaceResource', 'place', full=True)
	
	class Meta(BackboneCompatibleResource.Meta): 
		queryset = Checkin.objects.all()
		resource_name = 'checkin'
	
	def dehydrate(self, bundle):
		result = super(CheckinResource, self).dehydrate(bundle)
		result.data['added'] = epoch(bundle.obj.added)
		return result

# TODO
class NoticeResource(BackboneCompatibleResource):
	class Meta(BackboneCompatibleResource.Meta): 
		resource_name = 'notices'
	
	def apply_filters(self, request, applicable_filters): 
		#print 'la'
		#base_object_list = super(NoticeResource, self).apply_filters(request, applicable_filters) 
		return Notice.objects.notices_for(request.user, on_site=True)
