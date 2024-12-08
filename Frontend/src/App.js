import './css/base.css';
import './css/main.css';
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import Home from "./pages/Home";
import Booking from "./pages/Booking";
import {LoadingProvider} from "./contexts/LoadingContext";
import {ConnectionContextProvider} from "./contexts/ConnectionContext";
import Admin from "./pages/Admin";
import Auth from "./pages/Auth";
import {AdminContextProvider} from "./contexts/AdminContext";
import {BookingContextProvider} from "./contexts/BookingContext";

function App() {
  return (
      <div className="app">
          <LoadingProvider>
              <ConnectionContextProvider>
                  <AdminContextProvider>
                      <BookingContextProvider>
                          <Router>
                              <Routes>
                                  <Route path="/" element={<Home />} />
                                  <Route path="/buchen" element={<Booking />} />
                                  <Route path="/admin" element={<Admin />} />
                                  <Route path="/auth" element={<Auth />} />
                              </Routes>
                          </Router>
                      </BookingContextProvider>
                  </AdminContextProvider>
              </ConnectionContextProvider>
          </LoadingProvider>
      </div>
  );
}

export default App;
