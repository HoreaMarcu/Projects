import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {DeviceDTO} from "../dtos/deviceDTO";
import {PersonDTO} from "../dtos/personDTO";

@Injectable({
  providedIn: 'root'
})
export class DeviceService {
  constructor(private http: HttpClient) { }

  getAllDevices(): Observable<DeviceDTO[]> {
    let url = 'http://localhost:8081/device/getAll';
    let authorization = 'Bearer ' + sessionStorage.getItem('token')
    console.log("auth la device: " + authorization)
    return this.http.get<DeviceDTO[]>(url, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': authorization,
      }
    });
  }

  getDeviceById(id : string): Observable<DeviceDTO> {
    let url = 'http://localhost:8081/device/getById/' + id;
    let authorization = 'Bearer ' + sessionStorage.getItem('token')

    return this.http.get<DeviceDTO>(url,{
      headers: {
        'Content-Type': 'application/json',
        'Authorization': authorization
      }
    })
  }

  deleteDevice(id: string): Observable<void> {
    let url = 'http://localhost:8081/device/deleteById/' + id;
    let authorization = 'Bearer ' + sessionStorage.getItem('token')

    return this.http.delete<any>(url,{
      headers: {
        'Content-Type': 'application/json',
        'Authorization': authorization
      }
    });
  }

  addDevice(deviceDto : DeviceDTO){
    let url = 'http://localhost:8081/device/insertDevice'
    let authorization = 'Bearer ' + sessionStorage.getItem('token')

    return this.http.post<any>(url, deviceDto, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': authorization
      }
    });
  }
  updateDevice(deviceDto: DeviceDTO){
    let url = 'http://localhost:8081/device/updateDevice'
    let authorization = 'Bearer ' + sessionStorage.getItem('token')

    return this.http.put<any>(url, deviceDto, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': authorization
      }
    });
  }
}
