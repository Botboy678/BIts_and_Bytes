import "./App.css";
import { createBrowserRouter, RouterProvider } from "react-router-dom";

const router = createBrowserRouter([{ path: "/", element: <App /> }]);

function App() {
  return (
    <div className="App">
      <p>Hello</p>
    </div>
  );
}

export default App;
