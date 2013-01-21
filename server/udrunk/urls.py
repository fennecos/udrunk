from django.conf import settings
from django.conf.urls.defaults import patterns, include, url
from django.contrib import admin
from django.contrib.staticfiles.urls import staticfiles_urlpatterns
from tastypie.api import Api
from notification import urls as notification_urls

from apps.place.api import PlaceResource
from apps.core.api import SimpleUserResource, LoginResource, SubscriptionResource, FeedResource, CheckinResource, NoticeResource

api = Api(api_name='v1')
api.register(PlaceResource())
api.register(CheckinResource())
api.register(SimpleUserResource())
api.register(LoginResource())
api.register(SubscriptionResource())
api.register(NoticeResource())
api.register(FeedResource())

urlpatterns = patterns('',
	(r'^m/', include('apps.mobile.urls')),
	(r'^api/', include(api.urls)),
	(r'^notification/', include(notification_urls)),
)

admin.autodiscover()

urlpatterns += patterns('',
	url(r'^admin/doc/', include('django.contrib.admindocs.urls')),
	url(r'^admin/', include(admin.site.urls)),
)
urlpatterns += staticfiles_urlpatterns()

if settings.DEBUG:
    urlpatterns += patterns('',
        url(r'^media/(?P<path>.*)$', 'django.views.static.serve', {
            'document_root': settings.MEDIA_ROOT,
        }),
   )
