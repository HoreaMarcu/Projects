import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {MessageDTO} from "../dtos/messageDTO";
import {ChatMessageDTO} from "../dtos/chatMessageDTO";


@Injectable({
  providedIn: 'root',
})
export class WebSocketService {
  private socket!: WebSocket;
  private socket2!: WebSocket;

  public openWebSocket(): void {
    let authorization = sessionStorage.getItem('token')
    let webSocket = `${'ws://localhost:8082/websocket'}?authorization=${authorization}`;
    this.socket = new WebSocket(webSocket);
  }

  public openWebSocket2(): void {
    let authorization = sessionStorage.getItem('token')
    let webSocket = `${'ws://localhost:8083/websocket'}?authorization=${authorization}`;
    this.socket2 = new WebSocket(webSocket);
    // this.socket2 = new WebSocket('ws://localhost:8083/websocket');
  }

  public sendMessage(message: string): void {
    this.socket.send(message);
    console.log('message sent successfully')
  }

  public sendMessage2(message: ChatMessageDTO): void {
    // let chatMessageDto: ChatMessageDTO = {senderId: "test",receiverId: "test",message: "first test message",seen: false}
    let jsonMessage = JSON.stringify(message);
    // if(message != null) jsonMessage = JSON.stringify(message);
    this.socket2.send(jsonMessage);
    console.log('message sent successfully to web socket 2')
  }

  public getMessages(): Observable<string> {
    return new Observable<string>((observer) => {
      this.socket.addEventListener('message', (event) => {
        observer.next(event.data);
      });
    });
  }

  public getMessages2(): Observable<string> {
    return new Observable<string>((observer) => {
      this.socket2.addEventListener('message', (event) => {
        observer.next(event.data);
      });
    });
  }
}
