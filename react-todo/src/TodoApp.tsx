import { BrowserRouter } from "react-router"
import { AppRouter } from "./router/AppRouter"
import { getEnvVariables } from "./helpers/getEnvVariables";

export const TodoApp = () => {
  console.log(getEnvVariables());
  
  return (
    <BrowserRouter>
        <AppRouter />
    </BrowserRouter>
  )
}
