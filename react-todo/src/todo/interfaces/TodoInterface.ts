export interface TodoInterface {
  id?: string;
  taskName: string;
  priority: string;
  dueDate?: Date | null;
  done: boolean;
}
