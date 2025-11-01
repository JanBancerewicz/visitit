import { Routes } from '@angular/router';

// Categories + Elements
import { CategoriesListPage } from './features/categories/pages/categories-list/categories-list.page';
import { CategoryNewPage } from './features/categories/pages/category-new/category-new.page';
import { CategoryEditPage } from './features/categories/pages/category-edit/category-edit.page';
import { CategoryDetailsPage } from './features/categories/pages/category-details/category-details.page';
import { ElementNewPage } from './features/elements/pages/element-new/element-new.page';
import { ElementEditPage } from './features/elements/pages/element-edit/element-edit.page';
import { ElementDetailsPage } from './features/elements/pages/element-details/element-details.page';

// Clients
import { ClientsListPage } from './features/clients/pages/clients-list/clients-list.page';
import { ClientNewPage } from './features/clients/pages/client-new/client-new.page';
import { ClientEditPage } from './features/clients/pages/client-edit/client-edit.page';

// Employees
import { EmployeesListPage } from './features/employees/pages/employees-list/employees-list.page';
import { EmployeeNewPage } from './features/employees/pages/employee-new/employee-new.page';
import { EmployeeEditPage } from './features/employees/pages/employee-edit/employee-edit.page';

// Rooms
import { RoomsListPage } from './features/rooms/pages/rooms-list/rooms-list.page';
import { RoomNewPage } from './features/rooms/pages/room-new/room-new.page';
import { RoomEditPage } from './features/rooms/pages/room-edit/room-edit.page';

export const routes: Routes = [
  // Home
  { path: '', redirectTo: 'categories', pathMatch: 'full' },

  // Categories (services)
  { path: 'categories', component: CategoriesListPage },
  { path: 'categories/new', component: CategoryNewPage },
  { path: 'categories/:catId/edit', component: CategoryEditPage },

  // Elements (reservations) under a category
  { path: 'categories/:catId/elements/new', component: ElementNewPage },
  { path: 'categories/:catId/elements/:elId/edit', component: ElementEditPage },
  { path: 'categories/:catId/elements/:elId', component: ElementDetailsPage },

  // Category details (after element routes to avoid ambiguity)
  { path: 'categories/:catId', component: CategoryDetailsPage },

  // Clients
  { path: 'clients', component: ClientsListPage },
  { path: 'clients/new', component: ClientNewPage },
  { path: 'clients/:id/edit', component: ClientEditPage },

  // Employees
  { path: 'employees', component: EmployeesListPage },
  { path: 'employees/new', component: EmployeeNewPage },
  { path: 'employees/:id/edit', component: EmployeeEditPage },

  // Rooms
  { path: 'rooms', component: RoomsListPage },
  { path: 'rooms/new', component: RoomNewPage },
  { path: 'rooms/:id/edit', component: RoomEditPage },

  // Fallback
  { path: '**', redirectTo: 'categories' }
];
