export interface MetricsInterface {
    averageTimeToFinishByPriority:  AverageTimeToFinishByPriority;
    averageEstimatedTimeToComplete: string;
}

export interface AverageTimeToFinishByPriority {
    Low:    string;
    Medium: string;
    High:   string;
}