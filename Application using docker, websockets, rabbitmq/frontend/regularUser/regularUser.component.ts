import {Component, OnInit} from "@angular/core";
import {DeviceDTO} from "../dtos/deviceDTO";
import {DeviceService} from "../services/device.service";
import {Global} from "../global";
import {AuthService} from "../authService";
import {Router} from "@angular/router";
import {UserService} from "../services/user.service";
import {take} from "rxjs";

@Component({
  selector: 'regularUser-page',
  templateUrl: './regularUser.component.html',
  styleUrls: ['./regularUser.component.scss']
})

export class RegularUserComponent implements OnInit{
  devices: DeviceDTO[] = [];
  seeAllDevices = false;

  constructor(private deviceService: DeviceService,
              private authService: AuthService,
              private router: Router,
              private userService: UserService) {
  }
  ngOnInit(): void {
    Global.activeUser = this.authService.activeUser
    console.log(Global.activeUser)
    this.loadDevices()
  }

  loadDevices() {
    this.deviceService.getAllDevices().subscribe(data => {
      this.devices = data;
    })
  }
  logout() {
    this.authService.activeUser = null;
    Global.activeUser = null;
    this.router.navigate(['login-page']);
  }

  goToChatWithAdmin(){

    this.userService.getAllUsers().pipe(take(1)).subscribe(data => {
      console.log(data)
      let users = data;
      for(let user of users){
        if(user.adminRole) this.router.navigate(['/chat-page', { userId: user.id }]);
      }
    });


  }

}
