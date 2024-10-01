export class ChatMessageDTO{
  senderId !: string;
  receiverId !: string;
  message !: string;
  seen !: boolean;

  constructor(senderId: string, receiverId: string, message: string, seen: boolean) {
    this.senderId = senderId;
    this.receiverId = receiverId;
    this.message = message;
    this.seen = seen;
  }
}
