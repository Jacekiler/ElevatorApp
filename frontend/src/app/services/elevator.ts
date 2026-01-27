import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ElevatorModel } from '../models/elevator.model';

@Injectable({
  providedIn: 'root'
})
export class ElevatorService {
  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  getAll(): Observable<ElevatorModel[]> {
    return this.http.get<ElevatorModel[]>(`${this.baseUrl}/elevators`);
  }

  getById(id: number): Observable<ElevatorModel> {
    return this.http.get<ElevatorModel>(`${this.baseUrl}/elevators/${id}`);
  }

  call(id: number, floor: number, direction: 'UP' | 'DOWN') {
    return this.http.post(`${this.baseUrl}/elevators/${id}/call`, { floor, direction });
  }

  select(id: number, floor: number) {
    return this.http.post(`${this.baseUrl}/elevators/${id}/select`, { floor });
  }

  openDoors(id: number) {
    return this.http.post(`${this.baseUrl}/elevators/${id}/doors/open`, {});
  }

  closeDoors(id: number) {
    return this.http.post(`${this.baseUrl}/elevators/${id}/doors/close`, {});
  }
}
