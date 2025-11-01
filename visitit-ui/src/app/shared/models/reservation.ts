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
  startDatetime: string; // "YYYY-MM-DDTHH:mm:ss"
  endDatetime: string;
  status: string;        // PLANNED / CONFIRMED / CANCELLED
  note: string;
}

export type CreateReservationDTO = Omit<Reservation, 'id'>;
export type UpdateReservationDTO = Partial<Omit<Reservation, 'id' | 'serviceId'>>;
