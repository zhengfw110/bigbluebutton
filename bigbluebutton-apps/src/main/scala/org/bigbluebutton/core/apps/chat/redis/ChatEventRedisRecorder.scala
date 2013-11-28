package org.bigbluebutton.core.apps.chat.redis

import org.bigbluebutton.conference.service.recorder.RecorderApplication
import org.bigbluebutton.core.api.OutMessageListener2
import org.bigbluebutton.core.api.IOutMessage
import org.bigbluebutton.conference.service.recorder.chat.PublicChatRecordEvent
import scala.collection.JavaConversions._

class ChatEventRedisRecorder(recorder: RecorderApplication) extends OutMessageListener2 {
    import org.bigbluebutton.core.apps.chat.messages._
    
  	def handleMessage(msg: IOutMessage) {
	  msg match {
	    case sendPublicMessageEvent: SendPublicMessageEvent => handleSendPublicMessageEvent(sendPublicMessageEvent)
	    case _ => // do nothing
	  }
	}
    
    private def handleSendPublicMessageEvent(msg: SendPublicMessageEvent) {
      if (msg.recorded) {
        val message = mapAsJavaMap(msg.message)

        val ev = new PublicChatRecordEvent();
		ev.setTimestamp(System.currentTimeMillis());
		ev.setMeetingId(msg.meetingID);
		ev.setSender(message.get("fromUserID"));
		ev.setMessage(message.get("message"));
		ev.setLocale(message.get("fromLang"));
		ev.setColor(message.get("fromColor"));
		
		recorder.record(msg.meetingID, ev);	
      }
    }
}