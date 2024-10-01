import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly activeUserKey = 'activeUser';

  get activeUser(): any {
    const storedUser = localStorage.getItem(this.activeUserKey);
    return storedUser ? JSON.parse(storedUser) : null;
  }

  set activeUser(user: any) {
    if (user) {
      localStorage.setItem(this.activeUserKey, JSON.stringify(user));
    } else {
      localStorage.removeItem(this.activeUserKey);
    }
  }
}
