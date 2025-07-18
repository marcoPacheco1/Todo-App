export interface MetricsInterface {
    averageTimeToFinishByPriority:  AverageTimeToFinishByPriority;
    averageEstimatedTimeToComplete: string;
}

export interface AverageTimeToFinishByPriority {
    LOW:    string;
    MEDIUM: string;
    HIGH:   string;
}