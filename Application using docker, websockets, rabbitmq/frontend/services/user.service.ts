import { HttpClient } from "@angular/common/http";
import { PersonDTO } from "../dtos/personDTO";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import {Global} from "../global";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private http: HttpClient) { }

  getUserById(id: string){
    let url = 'http://localhost:8080/person/' + id;
    let authorization = 'Bearer ' + sessionStorage.getItem('token')

    return this.http.get<PersonDTO>(url,{
      headers: {
        'Content-Type': 'application/json',
        'Authorization': authorization
      }
    })
  }

  getUserByToken(token: string){
    let url = 'http://localhost:8080/person/getPersonByToken/' + token;
    let authorization = 'Bearer ' + sessionStorage.getItem('token')

    return this.http.get<PersonDTO>(url,{
      headers: {
        'Content-Type': 'application/json',
        'Authorization': authorization
      }
    })
  }

  getAllUsers(): Observable<PersonDTO[]> {
    let url = 'http://localhost:8080/person';
    let authorization = 'Bearer ' + sessionStorage.getItem('token');
    console.log(authorization);
    return this.http.get<PersonDTO[]>(url, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': authorization,
      }
    });
  }

  deleteUser(id: string): Observable<void> {
    let url = 'http://localhost:8080/person/' + id;
    let authorization = 'Bearer ' + sessionStorage.getItem('token')
    return this.http.delete<void>(url,{
      headers: {
        'Content-Type': 'application/json',
        'Authorization': authorization
      }
    });
  }

  insertUser(personDto: PersonDTO){
    let url = 'http://localhost:8080/person/insertPerson';
    // let authorization = 'Bearer ' + sessionStorage.getItem('token')
    return this.http.post<any>(url,personDto,{
      headers: {
        'Content-Type': 'application/json'
      }
    })
  }
}
