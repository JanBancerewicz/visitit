import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Category } from '../../shared/models/category';

@Injectable({ providedIn: 'root' })
export class CategoryService {
  // było: /api/categories
  private base = '/api/services';

  constructor(private http: HttpClient) {}

  list(): Observable<Category[]> {
    return this.http.get<Category[]>(this.base).pipe(
      catchError(err => { console.error('GET /api/services failed', err); return of([]); })
    );
  }

  get(id: string): Observable<Category> {
    return this.http.get<Category>(`${this.base}/${id}`);
  }

  create(dto: Partial<Category>): Observable<Category> {
    return this.http.post<Category>(this.base, dto).pipe(
      tap(() => console.log('Service created')),
      catchError(err => { console.error('POST /api/services failed', err); return throwError(() => err); })
    );
  }

  update(id: string, dto: Partial<Category>): Observable<Category> {
    return this.http.put<Category>(`${this.base}/${id}`, dto).pipe(
      tap(() => console.log('Service updated')),
      catchError(err => { console.error(`PUT /api/services/${id} failed`, err); return throwError(() => err); })
    );
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`).pipe(
      tap(() => console.log('Service deleted')),
      catchError(err => { console.error(`DELETE /api/services/${id} failed`, err); return throwError(() => err); })
    );
  }
}
