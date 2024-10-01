import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {AdministratorComponent} from "./administrator/administrator.component";
import {RouterModule, Routes} from "@angular/router";
import {HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {UserService} from "./services/user.service";
import {LoginComponent} from "./login/login.component";
import {DeviceService} from "./services/device.service";
import {RegisterComponent} from "./register/register.component";
import {AuthService} from "./authService";
import {RegularUserComponent} from "./regularUser/regularUser.component";
import {WebSocketService} from "./services/websocket.service";
import {WebSocketComponent} from "./websocket/websocket.component";
import {WebSocketComponent2} from "./websocket/websocket2.component";
import {ChatComponent} from "./chat/chat.component";


const appRoute: Routes = [
  {path:'',redirectTo: 'login-page',pathMatch: 'full'},
  {path: 'admin-page', component: AdministratorComponent },
  {path: 'login-page', component: LoginComponent},
  {path: 'register-page', component: RegisterComponent},
  {path: 'regularUser-page', component: RegularUserComponent},
  {
    path: 'chat-page',
    component: ChatComponent,
    children: [
      { path: ':userId', component: ChatComponent }
    ]
  }
];
@NgModule({
  declarations: [
    AppComponent,
    AdministratorComponent,
    LoginComponent,
    RegisterComponent,
    RegularUserComponent,
    WebSocketComponent,
    WebSocketComponent2,
    ChatComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    RouterModule.forRoot(appRoute),
    ReactiveFormsModule,
    FormsModule,
  ],
  providers: [UserService, DeviceService, AuthService, WebSocketService],
  bootstrap: [AppComponent]
})
export class AppModule { }
