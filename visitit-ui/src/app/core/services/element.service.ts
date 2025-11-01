import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

export interface ReservationList {
  id: string;
  clientName: string;
  employeeName: string;
  serviceName: string;
  roomName: string;
  status: string;
}

export interface Reservation {
  id: string;
  clientId: string;
  employeeId: string;
  serviceId: string;
  roomId: string;
  startDatetime: string; // ISO 'yyyy-MM-ddTHH:mm:ss'
  endDatetime: string;
  status: string;
  note?: string;
}

export type ReservationCreate = Omit<Reservation, 'id'>;

@Injectable({ providedIn: 'root' })
export class ElementService {
  private http = inject(HttpClient);

  listByCategory(serviceId: string): Observable<ReservationList[]> {
    return this.http.get<ReservationList[]>(`/api/reservations/by-service/${serviceId}`).pipe(
      catchError(err => { console.error('by-service failed', err); return of([]); })
    );
  }

  get(reservationId: string): Observable<Reservation> {
    return this.http.get<Reservation>(`/api/reservations/${reservationId}`);
  }

  create(dto: ReservationCreate): Observable<Reservation> {
    return this.http.post<Reservation>('/api/reservations', dto);
  }

  update(id: string, dto: ReservationCreate): Observable<Reservation> {
    return this.http.put<Reservation>(`/api/reservations/${id}`, dto);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`/api/reservations/${id}`);
  }
}
