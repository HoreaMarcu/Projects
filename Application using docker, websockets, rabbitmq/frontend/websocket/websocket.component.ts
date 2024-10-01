import {AfterViewInit, Component, OnInit} from '@angular/core';
import {WebSocketService} from "../services/websocket.service";
import {MessageDTO} from "../dtos/messageDTO";
import {DeviceService} from "../services/device.service";
import {take} from "rxjs";
import {DeviceDTO} from "../dtos/deviceDTO";
import {Global} from "../global";
import {AuthService} from "../authService";

@Component({
  selector: 'app-websocket',
  template: `
    <div>
<!--      <button (click)="sendMessage()">Send Message</button>-->
      <div *ngFor="let message of messages">Device with id: {{ message }} has a higher consumption than its maximum</div>
    </div>
  `,
})
export class WebSocketComponent implements OnInit, AfterViewInit {
  messages: string[] = [];

  constructor(private webSocketService: WebSocketService,
              private deviceService: DeviceService,
              private authService: AuthService) {}

  ngOnInit(): void {
    Global.activeUser = this.authService.activeUser
    console.log("STARTED WEBSOCKET COMP\n")
    this.webSocketService.openWebSocket();
    // this.webSocketService.openWebSocket2();
  }

  sendMessage(): void {
    this.webSocketService.sendMessage('Hello from Angular!');
  }


  ngAfterViewInit(): void {
    Global.activeUser = this.authService.activeUser
    this.webSocketService.getMessages().subscribe((message) => {
      console.log(message)
      const regex = /device_id=([a-f0-9-]+),/;

      const match = regex.exec(message);
      if(match) {
        const actualDeviceId = match[1];
        this.deviceService.getDeviceById(actualDeviceId).pipe(take(1)).subscribe(data => {
          let device = data
          console.log(device)
          console.log("TREBUIE SA FIE EGALE:")
          console.log(device?.personDTO)
          console.log(this.authService.activeUser)
          if (device && device.personDTO.id === this.authService.activeUser.id) {
            console.log(Global.activeUser)
            this.messages.push(actualDeviceId);
          }
        });


      }
    });

  }
}
