import {Component} from "@angular/core";
import {UserService} from "../services/user.service";
import {DeviceDTO} from "../dtos/deviceDTO";
import {take} from "rxjs";
import {PersonDTO} from "../dtos/personDTO";
import {Router} from "@angular/router";
import {Global} from "../global";
import {AuthService} from "../authService";

@Component({
  selector: 'register-page',
  templateUrl: './register.component.html',
  styleUrls:['./register.component.scss']
})
export class RegisterComponent{

  constructor(private userService: UserService,
              private router: Router,
              private authService: AuthService) {
  }


  insertUser(){
    const nameInput = document.querySelector('#nameInput') as HTMLInputElement;
    const passwordInput = document.querySelector('#passwordInput') as HTMLInputElement;
    const addressInput = document.querySelector('#addressInput') as HTMLInputElement;
    const ageInput = document.querySelector('#ageInput') as HTMLInputElement;
    const adminRoleInput = document.querySelector('#adminRoleInput') as HTMLInputElement;
    let userDto = new PersonDTO();
    userDto.name = nameInput.value;
    userDto.password = passwordInput.value;
    userDto.address = addressInput.value;
    userDto.age = +ageInput.value;
    userDto.adminRole = adminRoleInput.checked;


    this.userService.insertUser(userDto).pipe(take(1)).subscribe(()=>{
      // this.authService.activeUser = userDto
      // Global.activeUser = this.authService.activeUser
      this.router.navigate(['login-page']);
    });
  }
}
