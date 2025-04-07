export interface TodoInterface {
  id: number;
  taskName: string;
  priority: 'Low' | 'Medium' | 'High';
  dueDate?: Date | null;
  done: boolean;
  }