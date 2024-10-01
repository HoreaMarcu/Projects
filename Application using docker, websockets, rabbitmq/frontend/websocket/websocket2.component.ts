import {AfterViewInit, Component, OnInit} from '@angular/core';
import {WebSocketService} from "../services/websocket.service";
import {MessageDTO} from "../dtos/messageDTO";
import {DeviceService} from "../services/device.service";
import {take} from "rxjs";
import {DeviceDTO} from "../dtos/deviceDTO";
import {Global} from "../global";
import {AuthService} from "../authService";
import {ChatMessageDTO} from "../dtos/chatMessageDTO";

@Component({
  selector: 'app-websocket2',
  template: `
    <div>
<!--      <button (click)="sendMessage()">Send Message 2</button>-->
    </div>
  `,
})
export class WebSocketComponent2 implements OnInit, AfterViewInit {
  messages: string[] = [];

  constructor(private webSocketService: WebSocketService,
              private deviceService: DeviceService,
              private authService: AuthService) {}

  ngOnInit(): void {
    Global.activeUser = this.authService.activeUser
    console.log("STARTED WEBSOCKET COMP\n")
    this.webSocketService.openWebSocket2();
  }

  sendMessage(): void {
    this.webSocketService.sendMessage2(new ChatMessageDTO("","","", false));
  }
  ngAfterViewInit(): void {
    Global.activeUser = this.authService.activeUser
    this.webSocketService.getMessages2().subscribe((message) => {
      console.log(message)
    })
  }
}
