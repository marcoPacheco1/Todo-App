export interface TodoFilter {
    taskName?: string;
    priority?: 'Low' | 'Medium' | 'High';
    state?: 'Todo' | 'InProgress' | 'Done';
  }