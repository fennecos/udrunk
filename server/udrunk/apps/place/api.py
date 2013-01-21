from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from tastypie import fields
from tastypie.resources import ModelResource
#from tastypie.contrib.gis.resources import ModelResource as GeoModelResource
from tastypie.authorization import Authorization
from tastypie.authentication import ApiKeyAuthentication
from tastypie.serializers import Serializer
from tastypie.validation import Validation
from tastypie.constants import ALL, ALL_WITH_RELATIONS
from django.contrib.gis.geos import Point

from apps.place.models import Place
from apps.core.api import BackboneCompatibleResource


#class PlaceResource(GeoModelResource):
class PlaceResource(BackboneCompatibleResource):
	#recents = fields.ToManyField('apps.place.api.CheckinResource', 'checkin_set')
	near = False
	
	class Meta(BackboneCompatibleResource.Meta):
		queryset = Place.objects.all()
		resource_name = 'place'
		filtering = {
            'near': ALL,
        }
	
	def apply_filters(self, request, applicable_filters): 
		base_object_list = super(PlaceResource, self).apply_filters(request, applicable_filters) 
		near = request.GET.get('near', None) 
		if near: 
			# TODO : call API
			# https://maps.googleapis.com/maps/api/place/search/json?key=AIzaSyCE8oW2DDBTKZF0rLQvdixhiMhJk9X8mpE&location=48.826249,2.298288&radius=500&sensor=false&types=bar|cafe|restaurant
			coords = near.split(',')
			point  = Point(float(coords[0]), float(coords[1]))
			base_object_list = base_object_list.distance(point).order_by('distance') 
		return base_object_list 
	
	def dehydrate(self, bundle):
		result = super(PlaceResource, self).dehydrate(bundle)
		result.data['geometry'] = {'x': bundle.obj.geometry.x, 'y': bundle.obj.geometry.y}
		return result
	
	# TODO : utile ?
	def hydrate(self, bundle):
		result = super(PlaceResource, self).hydrate(bundle)
		try:
			result.data['geometry'] = Point(float(result.data['geo']['x']), float(result.data['geo']['y']))
			del result.data['geo']
		except:
			pass
		return result