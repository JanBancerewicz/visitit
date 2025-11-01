import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {map, Observable, of, throwError} from 'rxjs';
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
  startDatetime: string;  // ISO 8601, np. "2025-11-01T12:00"
  endDatetime: string;    // ISO 8601
  status: string;         // np. PLANNED / CONFIRMED / CANCELLED
  note: string;
}

@Injectable({ providedIn: 'root' })
export class ElementService {
  private readonly base = '/api/reservations';

  constructor(private http: HttpClient) {}

  // listByCategory(serviceId: string): Observable<ReservationList[]> {
  //   return this.http
  //     .get<ReservationList[]>(`${this.base}/by-service/${serviceId}`)
  //     .pipe(
  //       catchError(err => {
  //         console.error(`GET ${this.base}/by-service/${serviceId} failed`, err);
  //         return of([]);
  //       })
  //     );
  // }

  listByCategory(serviceId: string): Observable<ReservationList[]> {
    return this.http.get<ReservationList[]>(`/api/reservations/by-service/${serviceId}`).pipe(
      catchError(err => {
        console.error('by-service failed', err);
        // awaryjnie: pobierz pełne rezerwacje i przemapuj tylko te z danym serviceId
        return this.http.get<Reservation[]>(`/api/reservations`).pipe(
          map(all => all
            .filter(r => r.serviceId === serviceId)
            .map(r => ({
              id: r.id,
              clientName: r.clientId,     // placeholder bez refów
              employeeName: r.employeeId, // jw.
              serviceName: '—',
              roomName: r.roomId,
              status: r.status
            } as ReservationList))
          ),
          catchError(() => of([]))
        );
      })
    );
  }


  get(_serviceId: string, reservationId: string): Observable<Reservation> {
    return this.http.get<Reservation>(`${this.base}/${reservationId}`);
  }

  createReservation(dto: Omit<Reservation, 'id'>): Observable<Reservation> {
    return this.http.post<Reservation>(this.base, dto).pipe(
      catchError(err => {
        console.error(`POST ${this.base} failed`, err);
        return throwError(() => err);
      })
    );
  }

  delete(reservationId: string) {
    return this.http.delete<void>(`/api/reservations/${reservationId}`);
  }

  updateReservation(reservationId: string, dto: Partial<Reservation>): Observable<Reservation> {
    return this.http.put<Reservation>(`${this.base}/${reservationId}`, dto).pipe(
      catchError(err => {
        console.error(`PUT ${this.base}/${reservationId} failed`, err);
        return throwError(() => err);
      })
    );
  }
}
