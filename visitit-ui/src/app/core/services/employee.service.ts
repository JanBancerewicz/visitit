import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Employee } from '../../shared/models/employee';

@Injectable({ providedIn: 'root' })
export class EmployeeService {
  constructor(private http: HttpClient) {}

  list(): Observable<Employee[]> {
    return this.http.get<Employee[]>('/api/employees').pipe(
      catchError(err => { console.error('employees list failed', err); return of([]); })
    );
  }

  get(id: string): Observable<Employee> {
    return this.http.get<Employee>(`/api/employees/${id}`);
  }

  create(dto: Omit<Employee, 'id'>): Observable<Employee> {
    return this.http.post<Employee>('/api/employees', dto);
  }

  update(id: string, dto: Partial<Omit<Employee, 'id'>>): Observable<Employee> {
    return this.http.put<Employee>(`/api/employees/${id}`, dto);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`/api/employees/${id}`);
  }
}
