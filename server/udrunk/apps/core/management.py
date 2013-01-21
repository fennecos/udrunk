###
# NOTIFICATIONS FIXTURES
###
import settings 
from django.db.models import signals


if "notification" in settings.INSTALLED_APPS:
	from notification import models as notification
	print "Creating notification types"
	
	def create_notice_types(app, created_models, verbosity, **kwargs):
		notification.create_notice_type("friends_invite", _("Invitation Received"), _("you have received an invitation"))
		notification.create_notice_type("friends_accept", _("Acceptance Received"), _("an invitation you sent has been accepted"))
		
		signals.post_syncdb.connect(create_notice_types, sender=notification)
		
	## Test
	'''
	from django.contrib.auth.models import User
	from notification.models import *
	user = User.objects.get(pk=1)
	#notification.send([user,], "friends_invite", {"from_user": user})
	notices = Notice.objects.notices_for(user, on_site=True)
	print notices
	'''

else:
	print "Skipping creation of NoticeTypes as notification app not found"
	
