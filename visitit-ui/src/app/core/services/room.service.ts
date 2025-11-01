import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Room } from '../../shared/models/room';

@Injectable({ providedIn: 'root' })
export class RoomService {
  constructor(private http: HttpClient) {}

  list(): Observable<Room[]> {
    return this.http.get<Room[]>('/api/rooms').pipe(
      catchError(err => { console.error('rooms list failed', err); return of([]); })
    );
  }

  get(id: string): Observable<Room> {
    return this.http.get<Room>(`/api/rooms/${id}`);
  }

  create(dto: Omit<Room, 'id'>): Observable<Room> {
    return this.http.post<Room>('/api/rooms', dto);
  }

  update(id: string, dto: Partial<Omit<Room, 'id'>>): Observable<Room> {
    return this.http.put<Room>(`/api/rooms/${id}`, dto);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`/api/rooms/${id}`);
  }
}
