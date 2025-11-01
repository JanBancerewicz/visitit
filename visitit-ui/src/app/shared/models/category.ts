export interface Category {
  id: string;              // UUID (nadawany przez backend)
  name: string;
  description?: string;
  durationMin: number;     // > 0
  price: number;           // >= 0
}
