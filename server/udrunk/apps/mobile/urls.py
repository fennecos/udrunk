# -*- coding: utf-8 -*-

from django.conf.urls.defaults import *

from udrunk.apps.mobile.views import *

urlpatterns = patterns('',
	url(r'^$', home_view, name="home"),
	url(r'^login/$', login_view, name="login"),
	url(r'^logout/$', logout_view, name="logout"),
	url(r'^subscribe/$', subscribe_view, name="subscribe"),
)