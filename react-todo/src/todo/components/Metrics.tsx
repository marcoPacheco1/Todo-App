import { useContext, useState } from "react";
import { TodoContext } from "../../context/TodoContext";
import { differenceInMinutes, differenceInSeconds, format } from "date-fns";
import { TodoInterface } from "../interfaces/TodoInterface";

// TODO: que sea Dinamico
export const Metrics = () => {

  const { metricModel } = useContext( TodoContext );

  return (
    <>
      <div>
        <div className="row align-items-center border rounded p-2">
          <div className="col">
            <h6 className="mb-0">Average time to finish tasks:</h6>
            <p className="mb-0">{metricModel.averageEstimatedTimeToComplete}</p>
          </div>
          
          <div className="col">
            <h6 className="mb-0">Average time to finish tasks by priority:</h6>
            <p className="mb-0">High: {metricModel.averageTimeToFinishByPriority.High}</p>
            <p className="mb-0">Medium: {metricModel.averageTimeToFinishByPriority.Medium}</p>
            <p className="mb-0">Low: {metricModel.averageTimeToFinishByPriority.Low}</p>
          </div>
        </div>
      </div>
    </>
  )
}
