# -*- coding: utf-8 -*-

from django.contrib import admin
from django.contrib.gis import admin as geoadmin
from apps.place import models as pm
import settings

media = settings.MEDIA_URL

geoadmin.site.register(pm.Place, geoadmin.GeoModelAdmin)
