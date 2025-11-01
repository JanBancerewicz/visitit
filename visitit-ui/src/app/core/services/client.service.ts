import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Client } from '../../shared/models/client';

@Injectable({ providedIn: 'root' })
export class ClientService {
  constructor(private http: HttpClient) {}

  list(): Observable<Client[]> {
    return this.http.get<Client[]>('/api/clients').pipe(
      catchError(err => { console.error('clients list failed', err); return of([]); })
    );
  }

  get(id: string): Observable<Client> {
    return this.http.get<Client>(`/api/clients/${id}`);
  }

  create(dto: Omit<Client, 'id'>): Observable<Client> {
    return this.http.post<Client>('/api/clients', dto);
  }

  update(id: string, dto: Partial<Omit<Client, 'id'>>): Observable<Client> {
    return this.http.put<Client>(`/api/clients/${id}`, dto);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`/api/clients/${id}`);
  }
}
