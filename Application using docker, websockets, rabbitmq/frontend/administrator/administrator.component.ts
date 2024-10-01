import {Component, OnInit} from "@angular/core";
import {UserService} from "../services/user.service";
import {PersonDTO} from "../dtos/personDTO";
import {DeviceService} from "../services/device.service";
import {DeviceDTO} from "../dtos/deviceDTO";
import {take} from "rxjs";
import {Global} from "../global";
import {AuthService} from "../authService";
import {Router} from "@angular/router";

@Component({
  selector: 'admin-page',
  templateUrl: './administrator.component.html',
  styleUrls: ['./administrator.component.scss']
})

export class AdministratorComponent implements OnInit {
  seeAllUsers = false;
  seeAllDevices = false;

  updateDeviceView = false;
  users: PersonDTO[] = [];

  devices: DeviceDTO[] = [];

  addDeviceView = false;

  constructor(private userService: UserService,
              private deviceService: DeviceService,
              private authService: AuthService,
              private router: Router) {
  }

  ngOnInit(): void {
    Global.activeUser = this.authService.activeUser;
    if(!this.authService.activeUser || !this.authService.activeUser.adminRole) this.router.navigate(['regularUser-page'])
    this.loadUsers();
    this.loadDevices();

    console.log(Global.activeUser)
  }

  loadUsers() {
    this.userService.getAllUsers().pipe(take(1)).subscribe(data => {
      console.log(data)
      this.users = data;
    });
  }

  loadDevices() {
    this.deviceService.getAllDevices().pipe(take(1)).subscribe(data => {
      this.devices = data;
    })
  }

  deleteUser(id: string) {
    this.userService.deleteUser(id).subscribe(() => {
      window.location.reload();
    });
  }

  deleteDevice(id: string) {
    this.deviceService.deleteDevice(id).subscribe(() => {
      window.location.reload();
    });
  }

  addDevice() {
    const descriptionInput = document.querySelector('#deviceDescription') as HTMLInputElement;
    const maxHourlyEnergyConsumptionInput = document.querySelector('#maxHourlyEnergyConsumption') as HTMLInputElement;
    const userIdInput = document.querySelector('#userID') as HTMLInputElement;
    let deviceDto = new DeviceDTO();
    deviceDto.description = descriptionInput.value;
    deviceDto.maxHourlyEnergyConsumption = +maxHourlyEnergyConsumptionInput.value;

    this.userService.getUserById(userIdInput.value).pipe(take(1)).subscribe( data =>{
      console.log("A MERS USER BY ID")
      deviceDto.personDTO = data
      this.deviceService.addDevice(deviceDto).pipe(take(1)).subscribe(() => {
        window.location.reload();
      });
    })


  }

  updateDevice() {
    const deviceIdUpdate = document.querySelector('#deviceIdUpdate') as HTMLInputElement;
    const descriptionInputUpdate = document.querySelector('#deviceDescriptionUpdate') as HTMLInputElement;
    const maxHourlyEnergyConsumptionInputUpdate = document.querySelector('#maxHourlyEnergyConsumptionUpdate') as HTMLInputElement;
    const userIdInputUpdate = document.querySelector('#userIDUpdate') as HTMLInputElement;
    let deviceDto = new DeviceDTO();
    deviceDto.id = deviceIdUpdate.value;
    deviceDto.description = descriptionInputUpdate.value;
    deviceDto.maxHourlyEnergyConsumption = +maxHourlyEnergyConsumptionInputUpdate.value;

    this.userService.getUserById(userIdInputUpdate.value).pipe(take(1)).subscribe( data =>{
      deviceDto.personDTO = data
      this.deviceService.updateDevice(deviceDto).pipe(take(1)).subscribe(() => {
        window.location.reload();
      });
    })

  }

  logout() {
    this.authService.activeUser = null;
    Global.activeUser = null;
    this.router.navigate(['login-page']);
  }

  goToChatWithUser(userId: string){
    this.router.navigate(['/chat-page', { userId: userId }]);
  }
}
