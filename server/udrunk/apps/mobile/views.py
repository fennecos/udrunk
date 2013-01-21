# -*- coding: utf-8 -*-
from django.conf import settings
from django.utils.translation import ugettext as _
from django.template import RequestContext
from django.template.loader import render_to_string
from django.shortcuts import render_to_response, get_object_or_404
from django.http import Http404, HttpResponseRedirect
from django.contrib.auth import authenticate, login, logout
from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User


@login_required(login_url='/m/login/')
def home_view(request):
	return render_to_response(
		'mobile/home.html',
		{},
		context_instance=RequestContext(request))

def login_view(request):
	if request.POST:
		username = request.POST.get('username')
		password = request.POST.get('password')
		next     = request.POST.get('next')
		if next is None: 
			next = '/'
		user = authenticate(username=username, password=password)
		if user is not None:
			if user.is_active:
				login(request, user)
				return HttpResponseRedirect(next)
			else:
				pass
	return render_to_response(
		'mobile/login.html',
		{},
		context_instance=RequestContext(request))
                
def logout_view(request):
	logout(request)
	return HttpResponseRedirect('/')

def subscribe_view(request):
	return HttpResponse('')