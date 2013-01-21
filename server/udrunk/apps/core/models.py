import settings
from django.db import models
from django.contrib.auth.models import User
from django.utils.translation import ugettext_lazy as _

from apps.place.models import Place


class Profile(models.Model):
	GENDER_CHOICES = (
		('M', _('Man')),
		('W', _('Woman')),
	)
	user   = models.ForeignKey(User, blank=True, null=True)
	avatar = models.ImageField(upload_to='avatars/')
	
	def get_avatar(self):
		if self.avatar:
			return '%s%s' % (settings.MEDIA_URL, self.avatar)
		return '%savatars/default.jpg' % settings.MEDIA_URL

class Friend(models.Model):
	user1    = models.ForeignKey(User, related_name='user1')
	user2    = models.ForeignKey(User, related_name='user2')
	state    = models.IntegerField(_('State'), default=0)
	added    = models.DateTimeField()
	
class Checkin(models.Model):
	place   = models.ForeignKey(Place)
	user    = models.ForeignKey(User)
	level   = models.IntegerField(_('Drunk level'))
	status  = models.CharField(_('Status'), max_length=255, blank=True, null=True)
	added   = models.DateTimeField(_('Date'), auto_now=True)
	
