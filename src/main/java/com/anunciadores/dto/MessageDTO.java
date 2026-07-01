package  com.anunciadores.dto;
import com.anunciadores.dto.ChatDTO;
import com.anunciadores.dto.ContactDTO;
import com.anunciadores.dto.FromDTO;
public class MessageDTO
{
public int message_id;
public FromDTO from;
public ChatDTO chat;
public MessageDTO(int message_id, FromDTO from, ChatDTO chat, int date, String text, ContactDTO contact) {
this.message_id = message_id;
this.from = from;
this.chat = chat;
this.date = date;
this.text = text;
this.contact = contact;
} public int date; public String text; public ContactDTO contact;
public MessageDTO() {}
public int getMessage_id() {
return this.message_id;
}
public void setMessage_id(int message_id) {
this.message_id = message_id;
}
public FromDTO getFrom() {
return this.from;
}
public void setFrom(FromDTO from) {
this.from = from;
}
public ChatDTO getChat() {
return this.chat;
}
public void setChat(ChatDTO chat) {
this.chat = chat;
}
public int getDate() {
return this.date;
}
public void setDate(int date) {
this.date = date;
}
public String getText() {
return this.text;
}
public void setText(String text) {
this.text = text;
}
public ContactDTO getContact() {
return this.contact;
}
public void setContact(ContactDTO contact) {
this.contact = contact;
}
}
