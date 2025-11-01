/** Pojedyncza usługa (service) z backendu /api/services */
export interface Category {
  /** UUID nadawany przez backend */
  id: string;
  /** Nazwa usługi, np. "Strzyżenie" */
  name: string;
  /** Opcjonalny opis */
  description?: string;
  /** Czas trwania w minutach, > 0 */
  durationMin: number;
  /** Cena brutto w PLN, >= 0 */
  price: number;
}
