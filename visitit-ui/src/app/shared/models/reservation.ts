/** Skrótowy widok rezerwacji używany na listach (by-service) */
export interface ReservationList {
  id: string;
  clientName: string;
  employeeName: string;
  serviceName: string;
  roomName: string;
  status: string;
}

/** Pełny obiekt rezerwacji */
export interface Reservation {
  id: string;
  clientId: string;
  employeeId: string;
  serviceId: string;
  roomId: string;
  /** ISO 8601, np. "2025-11-01T12:00" lub z sekundami "2025-11-01T12:00:00" */
  startDatetime: string;
  endDatetime: string;
  status: string; // np. PLANNED / CONFIRMED / CANCELLED
  note: string;
}

/** DTO do tworzenia rezerwacji */
export type CreateReservationDTO = Omit<Reservation, 'id'>;

/** DTO do częściowej edycji rezerwacji */
export type UpdateReservationDTO = Partial<Omit<Reservation, 'id' | 'serviceId'>>;
