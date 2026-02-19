import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Dashboard from './Dashboard';
import WhatIfAnalysis from './WhatIfAnalysis';
import Architecture from './Architecture';
import CreateDevice from './CreateDevice';
import LandingPage from './LandingPage';

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/create" element={<CreateDevice />} />
        <Route path="/architecture" element={<Architecture />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/what-if" element={<WhatIfAnalysis />} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  );
}