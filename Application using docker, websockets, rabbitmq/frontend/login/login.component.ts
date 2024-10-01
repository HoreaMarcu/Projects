import {Component, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {UserService} from "../services/user.service";
import {PersonDTO} from "../dtos/personDTO";
import {AuthService} from "../authService";
import {Global} from "../global";
import {HttpClient} from "@angular/common/http";
import {take} from "rxjs";


@Component({
  selector: 'login-page',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})

export class LoginComponent implements OnInit {
  allUsers!: PersonDTO[]
  model: any = {};
  sessionId: any = "";
  constructor(private router: Router,
              private userService: UserService,
              private authService: AuthService,
              private http: HttpClient) {
  }

  ngOnInit(): void {
    // this.userService.getAllUsers().subscribe(data => {
    //   this.allUsers = data;
    // });
  }

  login() {
    let name = (document.getElementById("name") as HTMLInputElement).value;
    let password = (document.getElementById("password") as HTMLInputElement).value;
    let url = 'http://localhost:8080/authenticate';
    this.http.post<any>(url, {
      username: name,
      password: password
    }).pipe(take(1)).subscribe(res => {
      if (res) {
        console.log(res)
        console.log(res!.jwt)
        this.sessionId = res!.jwt;
        Global.token = res!.jwt;
        sessionStorage.setItem(
          'token',
          this.sessionId
        );
        this.userService.getUserByToken(res!.jwt).pipe(take(1)).subscribe(data => {
          this.authService.activeUser = data;
          Global.activeUser = this.authService.activeUser;
          if(this.authService.activeUser.adminRole) this.router.navigate(['admin-page']);
          else this.router.navigate(['regularUser-page']);
        })
        // this.router.navigate(['admin-page']);

        // let name = (document.getElementById("name") as HTMLInputElement).value;
        // let password = (document.getElementById("password") as HTMLInputElement).value;
        // for (let userDto of this.allUsers) {
        //   if (userDto.name === name && userDto.password === password) {
        //     this.authService.activeUser = userDto
        //     Global.activeUser = this.authService.activeUser
        //     if (userDto.adminRole)
        //       this.router.navigate(['admin-page']);
        //     else
        //       this.router.navigate(['regularUser-page']);
        //   }
        // }
        // this.router.navigate(['']);
      } else {
        alert("Authentication failed.")
      }
    });







  }

  goToRegister() {
    this.router.navigate(['register-page']);
  }
}
