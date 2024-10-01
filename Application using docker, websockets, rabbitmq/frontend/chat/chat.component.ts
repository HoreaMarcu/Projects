import {AfterViewInit, Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {MessageDTO} from "../dtos/messageDTO";
import {WebSocketService} from "../services/websocket.service";
import {Global} from "../global";
import {AuthService} from "../authService";
import {take} from "rxjs";
import {ChatMessageDTO} from "../dtos/chatMessageDTO";

@Component({
  selector: 'chat-page',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})

export class ChatComponent implements OnInit, AfterViewInit{

  messages : ChatMessageDTO[] = []
  newMessageText: string = '';
  receiverId : string = ''
  constructor(private route: ActivatedRoute,
              private webSocketService: WebSocketService,
              private authService: AuthService) { }
  ngOnInit() {
    Global.activeUser = this.authService.activeUser
    this.route.params.subscribe(params => {
      console.log(params)
      this.receiverId = params['userId'];
      console.log(this.receiverId)
    });
    this.webSocketService.openWebSocket2();
  }

  handleIncomingMessage(message: string) {
    if (message.startsWith('Server say')) {
      console.log('Server message:', message);
      return;
    }

    let chatMessage = JSON.parse(message);
    if((chatMessage.senderId === this.authService.activeUser.id && chatMessage.receiverId === this.receiverId) ||
        (chatMessage.receiverId === this.authService.activeUser.id && chatMessage.senderId === this.receiverId)) {
      this.messages.push(chatMessage);
      console.log(chatMessage);
    }
  }
  ngAfterViewInit(): void {
    this.messages = []
    this.webSocketService.getMessages2().subscribe((message) => {
      this.handleIncomingMessage(message);
    })
  }
  sendMessage() {
      let id = this.authService.activeUser.id;
      const newMessage = new ChatMessageDTO(id, this.receiverId, this.newMessageText, false);
      console.log(newMessage)
      this.webSocketService.sendMessage2(newMessage)
      this.newMessageText = '';
  }

}
