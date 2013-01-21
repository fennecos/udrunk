from django.db import models
from django.contrib.gis.db import models as geomodels
from django.contrib.auth.models import User

from django.utils.translation import ugettext_lazy as _

class Place(models.Model):
	name = geomodels.CharField(_('Name'), max_length=255)
	api_id = geomodels.CharField(_('API ID'), max_length=255, blank=True, null=True)
	city = geomodels.CharField(_('City'), max_length=255, blank=True, null=True)
	geometry = geomodels.PointField(srid=4326)
	objects = geomodels.GeoManager()
	
	def __unicode__(self):
		return u'%s' % self.name

