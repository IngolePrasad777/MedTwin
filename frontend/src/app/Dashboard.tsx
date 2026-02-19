import { useNavigate } from 'react-router-dom';
import { 
  Activity, 
  Wind, 
  Battery, 
  Thermometer, 
  Cpu, 
  AlertTriangle, 
  Settings, 
  Bell,
  Play,
  Pause,
  FlaskConical
} from 'lucide-react';

export default function Dashboard() {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-gray-900 text-white font-sans selection:bg-blue-500/30">
      
      {/* Top Navigation */}
      <header className="px-6 py-4 border-b border-gray-800 bg-gray-950">
        <div className="max-w-[1800px] mx-auto flex justify-between items-center">
          <div className="flex items-center gap-4">
            <div className="flex items-center gap-2 cursor-pointer" onClick={() => navigate('/')}>
              <div className="w-8 h-8 bg-gradient-to-br from-blue-600 to-blue-800 rounded-lg flex items-center justify-center">
                <Cpu className="w-5 h-5 text-white" />
              </div>
              <span className="text-lg font-semibold text-white">MedTwin</span>
            </div>
            <div className="h-6 w-px bg-gray-800"></div>
            <div className="flex items-center gap-2 px-3 py-1 bg-blue-500/10 border border-blue-500/20 rounded-full">
              <div className="w-2 h-2 bg-blue-500 rounded-full animate-pulse"></div>
              <span className="text-xs font-medium text-blue-400 uppercase tracking-wide">Live Connected</span>
            </div>
          </div>
          <nav className="hidden md:flex items-center gap-8">
            <a href="#" onClick={(e) => { e.preventDefault(); navigate('/dashboard'); }} className="text-sm text-white font-medium transition-colors">Dashboard</a>
            <a href="#" className="text-sm text-gray-400 hover:text-white transition-colors">Projects</a>
            <a href="#" className="text-sm text-gray-400 hover:text-white transition-colors">Documentation</a>
            <div className="flex items-center gap-3">
              <button className="p-2 text-gray-400 hover:text-white hover:bg-gray-800 rounded-full transition-all relative">
                <Bell className="w-5 h-5" />
                <span className="absolute top-2 right-2 w-2 h-2 bg-red-500 rounded-full border-2 border-gray-900"></span>
              </button>
              <button className="p-2 text-gray-400 hover:text-white hover:bg-gray-800 rounded-full transition-all">
                <Settings className="w-5 h-5" />
              </button>
              <div className="w-8 h-8 bg-gradient-to-tr from-blue-500 to-purple-500 rounded-full border border-gray-700"></div>
            </div>
          </nav>
        </div>
      </header>

      <main className="max-w-[1800px] mx-auto p-6 md:p-8">
        
        {/* Page Header */}
        <div className="flex justify-between items-end mb-10">
          <div>
            <div className="flex items-center gap-4 mb-3">
              <h1 className="text-4xl font-light text-white">Ventilator <span className="font-bold">XT-9000</span></h1>
              <div className="flex items-center gap-2 px-4 py-2 bg-green-500/20 border border-green-500/30 rounded-full">
                <div className="w-3 h-3 bg-green-400 rounded-full animate-pulse"></div>
                <span className="text-sm font-bold text-green-400 uppercase tracking-wide">Live Connected</span>
              </div>
            </div>
            <div className="flex items-center gap-6 text-sm text-gray-400">
              <span>System ID: <span className="font-mono text-gray-300 font-semibold">DEV-2024-X9</span></span>
              <div className="w-1 h-1 bg-gray-600 rounded-full"></div>
              <span>Firmware: <span className="font-mono text-gray-300 font-semibold">v2.1.0</span></span>
              <div className="w-1 h-1 bg-gray-600 rounded-full"></div>
              <span>Uptime: <span className="font-mono text-gray-300 font-semibold">72h 14m</span></span>
            </div>
          </div>
          <div className="flex gap-4">
            <button 
              onClick={() => navigate('/what-if')}
              className="flex items-center gap-3 px-6 py-3 border-2 border-gray-700 bg-gray-800/50 hover:bg-gray-800 hover:border-gray-600 text-gray-300 hover:text-white rounded-xl text-sm font-semibold transition-all duration-300 hover:scale-105">
                <FlaskConical className="w-5 h-5" />
                What-If Analysis
              </button>
              <button 
                onClick={() => navigate('/what-if')}
                className="inline-flex items-center gap-3 px-8 py-3 bg-gradient-to-r from-blue-600 via-blue-700 to-purple-700 text-white rounded-xl font-bold shadow-2xl shadow-blue-500/30 hover:shadow-3xl hover:shadow-blue-500/50 transition-all duration-500 hover:scale-105 hover:-translate-y-1">
                <Play className="w-5 h-5" />
                Run Simulation
              </button>
          </div>
        </div>

        {/* Dashboard Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
          
          {/* Main Visualization Area (Large) */}
          <div className="lg:col-span-3 grid grid-rows-[3fr_2fr] gap-6">
            
            {/* 3D Model Viewport (Enhanced) */}
            <div className="bg-gradient-to-br from-gray-950 via-gray-900 to-gray-950 border border-gray-800 rounded-3xl relative overflow-hidden group shadow-2xl">
              <div className="absolute inset-0 bg-[radial-gradient(ellipse_at_center,_var(--tw-gradient-stops))] from-blue-900/20 via-gray-950 to-gray-950"></div>
              
              {/* Enhanced Grid Background */}
              <div className="absolute inset-0 bg-[linear-gradient(rgba(59,130,246,0.1)_1px,transparent_1px),linear-gradient(90deg,rgba(59,130,246,0.1)_1px,transparent_1px)] bg-[length:40px_40px] opacity-30"></div>

              {/* Central Device representation - Enhanced */}
              <div className="absolute inset-0 flex items-center justify-center">
                <div className="relative">
                  {/* Outer Ring */}
                  <div className="w-80 h-80 border-2 border-blue-500/20 rounded-full animate-[spin_20s_linear_infinite] opacity-40"></div>
                  {/* Middle Ring */}
                  <div className="absolute inset-0 w-64 h-64 m-auto border-2 border-purple-500/30 rounded-full animate-[spin_15s_linear_infinite_reverse] opacity-50"></div>
                  {/* Inner Ring */}
                  <div className="absolute inset-0 w-48 h-48 m-auto border-2 border-cyan-500/40 rounded-full animate-[spin_10s_linear_infinite] opacity-60"></div>
                  
                  {/* Central Icon */}
                  <div className="absolute inset-0 flex items-center justify-center">
                    <div className="w-24 h-24 bg-gradient-to-br from-blue-500 via-purple-500 to-cyan-500 rounded-2xl flex items-center justify-center shadow-2xl shadow-blue-500/50">
                      <Cpu className="w-12 h-12 text-white drop-shadow-lg" />
                    </div>
                  </div>
                  
                  {/* Floating Status Cards */}
                  <div className="absolute -top-16 -right-20 bg-gray-900/95 backdrop-blur-md border border-gray-700 px-4 py-3 rounded-xl shadow-xl flex items-center gap-3 animate-pulse">
                    <div className="w-3 h-3 bg-green-500 rounded-full animate-pulse"></div>
                    <div>
                      <div className="text-xs font-mono text-gray-300">Motor Speed</div>
                      <div className="text-sm font-bold text-white">1,247 RPM</div>
                    </div>
                  </div>
                  
                  <div className="absolute -bottom-12 -left-16 bg-gray-900/95 backdrop-blur-md border border-gray-700 px-4 py-3 rounded-xl shadow-xl flex items-center gap-3 animate-pulse" style={{animationDelay: '1s'}}>
                    <div className="w-3 h-3 bg-blue-500 rounded-full animate-pulse"></div>
                    <div>
                      <div className="text-xs font-mono text-gray-300">Flow Rate</div>
                      <div className="text-sm font-bold text-white">47.2 L/min</div>
                    </div>
                  </div>
                  
                  <div className="absolute top-0 -left-24 bg-gray-900/95 backdrop-blur-md border border-gray-700 px-4 py-3 rounded-xl shadow-xl flex items-center gap-3 animate-pulse" style={{animationDelay: '2s'}}>
                    <div className="w-3 h-3 bg-purple-500 rounded-full animate-pulse"></div>
                    <div>
                      <div className="text-xs font-mono text-gray-300">Pressure</div>
                      <div className="text-sm font-bold text-white">15.8 cmH₂O</div>
                    </div>
                  </div>
                </div>
              </div>

              {/* Enhanced Control Panel */}
              <div className="absolute bottom-6 left-6 flex gap-3">
                <button className="p-3 bg-gray-800/80 hover:bg-gray-700 rounded-xl text-gray-400 hover:text-white transition-all duration-300 backdrop-blur-sm border border-gray-700 group" title="Explode View">
                  <Settings className="w-5 h-5 group-hover:rotate-90 transition-transform duration-300" />
                </button>
                <div className="flex items-center gap-3 px-4 py-3 bg-gray-800/80 backdrop-blur-sm rounded-xl text-sm font-mono text-gray-400 border border-gray-700">
                  <span className="w-2 h-2 bg-green-500 rounded-full animate-pulse"></span>
                  <span className="text-green-400 font-semibold">REAL-TIME SYNC</span>
                </div>
              </div>
              
              {/* Performance Indicator */}
              <div className="absolute top-6 right-6 bg-gray-800/80 backdrop-blur-sm border border-gray-700 px-4 py-3 rounded-xl">
                <div className="text-xs text-gray-400 mb-1">System Health</div>
                <div className="flex items-center gap-2">
                  <div className="w-16 h-2 bg-gray-700 rounded-full overflow-hidden">
                    <div className="w-[94%] h-full bg-gradient-to-r from-green-500 to-green-400 rounded-full"></div>
                  </div>
                  <span className="text-sm font-bold text-green-400">94%</span>
                </div>
              </div>
            </div>

            {/* Time Series Charts */}
            <div className="grid grid-cols-2 gap-6 bg-gray-950 border border-gray-800 rounded-2xl p-6">
              <div>
                <h3 className="text-sm font-semibold text-gray-400 mb-4 flex items-center gap-2">
                  <Wind className="w-4 h-4 text-blue-400" />
                  Airflow Pressure (cmH2O)
                </h3>
                <div className="h-40 flex items-end gap-1">
                  {[40, 65, 45, 30, 60, 75, 50, 45, 60, 70, 55, 40, 35, 50, 65, 75, 60, 50, 45, 55].map((h, i) => (
                    <div 
                      key={i} 
                      className="flex-1 bg-blue-500/20 hover:bg-blue-500/40 transition-colors rounded-t-sm relative group"
                      style={{ height: `${h}%` }}
                    >
                      <div className="absolute bottom-full left-1/2 -translate-x-1/2 mb-1 px-2 py-1 bg-gray-800 text-xs rounded opacity-0 group-hover:opacity-100 transition-opacity whitespace-nowrap z-10 pointer-events-none">
                        {h} cmH20
                      </div>
                    </div>
                  ))}
                </div>
              </div>
              <div>
                <h3 className="text-sm font-semibold text-gray-400 mb-4 flex items-center gap-2">
                  <Activity className="w-4 h-4 text-purple-400" />
                  System Load (%)
                </h3>
                <div className="h-40 flex items-end gap-1">
                  {[20, 25, 22, 30, 25, 28, 35, 40, 38, 35, 30, 28, 25, 22, 20, 18, 22, 25, 28, 30].map((h, i) => (
                    <div 
                      key={i} 
                      className="flex-1 bg-purple-500/20 hover:bg-purple-500/40 transition-colors rounded-t-sm"
                      style={{ height: `${h}%` }}
                    ></div>
                  ))}
                </div>
              </div>
            </div>
          </div>

          {/* Right Sidebar (Enhanced Metrics) */}
          <div className="lg:col-span-1 space-y-6">
            
            {/* Status Card - Enhanced */}
            <div className="bg-gradient-to-br from-gray-900 via-gray-800 to-gray-900 rounded-2xl p-6 border border-gray-700 shadow-2xl">
              <div className="flex items-center justify-between mb-6">
                <h3 className="text-lg font-bold text-gray-200 uppercase tracking-wider">Live Metrics</h3>
                <div className="w-3 h-3 bg-green-400 rounded-full animate-pulse"></div>
              </div>
              
              <div className="space-y-8">
                <div className="group cursor-pointer">
                  <div className="flex items-center justify-between mb-3">
                    <div className="flex items-center gap-4">
                      <div className="p-3 bg-gradient-to-br from-blue-500 to-blue-600 rounded-xl text-blue-100 group-hover:scale-110 transition-transform duration-300 shadow-lg">
                        <Battery className="w-6 h-6" />
                      </div>
                      <div>
                        <div className="text-3xl font-bold font-mono text-white">92%</div>
                        <div className="text-sm text-gray-400 font-medium">Main Battery</div>
                      </div>
                    </div>
                    <div className="text-right">
                      <span className="text-sm text-green-400 bg-green-400/10 px-3 py-1 rounded-full font-semibold">+2%</span>
                      <div className="text-xs text-gray-500 mt-1">vs last hour</div>
                    </div>
                  </div>
                  <div className="w-full bg-gray-700 h-2 rounded-full overflow-hidden">
                    <div className="w-[92%] h-full bg-gradient-to-r from-blue-500 to-blue-400 rounded-full"></div>
                  </div>
                </div>

                <div className="group cursor-pointer">
                  <div className="flex items-center justify-between mb-3">
                    <div className="flex items-center gap-4">
                      <div className="p-3 bg-gradient-to-br from-red-500 to-orange-500 rounded-xl text-red-100 group-hover:scale-110 transition-transform duration-300 shadow-lg">
                        <Thermometer className="w-6 h-6" />
                      </div>
                      <div>
                        <div className="text-3xl font-bold font-mono text-white">38.5°C</div>
                        <div className="text-sm text-gray-400 font-medium">Core Temp</div>
                      </div>
                    </div>
                    <div className="text-right">
                      <span className="text-sm text-amber-400 bg-amber-400/10 px-3 py-1 rounded-full font-semibold">Warm</span>
                      <div className="text-xs text-gray-500 mt-1">Normal range</div>
                    </div>
                  </div>
                  <div className="w-full bg-gray-700 h-2 rounded-full overflow-hidden">
                    <div className="w-[64%] h-full bg-gradient-to-r from-orange-500 to-red-500 rounded-full"></div>
                  </div>
                </div>

                <div className="group cursor-pointer">
                  <div className="flex items-center justify-between mb-3">
                    <div className="flex items-center gap-4">
                      <div className="p-3 bg-gradient-to-br from-purple-500 to-purple-600 rounded-xl text-purple-100 group-hover:scale-110 transition-transform duration-300 shadow-lg">
                        <Cpu className="w-6 h-6" />
                      </div>
                      <div>
                        <div className="text-3xl font-bold font-mono text-white">1.2</div>
                        <div className="text-sm text-gray-400 font-medium">TFLOPS Load</div>
                      </div>
                    </div>
                    <div className="text-right">
                      <span className="text-sm text-blue-400 bg-blue-400/10 px-3 py-1 rounded-full font-semibold">Optimal</span>
                      <div className="text-xs text-gray-500 mt-1">Peak: 2.4</div>
                    </div>
                  </div>
                  <div className="w-full bg-gray-700 h-2 rounded-full overflow-hidden">
                    <div className="w-[50%] h-full bg-gradient-to-r from-purple-500 to-purple-400 rounded-full"></div>
                  </div>
                </div>
              </div>
            </div>

            {/* AI Insight Card */}
            <div className="bg-gradient-to-br from-indigo-900/30 to-purple-900/30 rounded-xl p-5 border border-indigo-500/20 backdrop-blur-sm">
              <div className="flex items-center gap-2 mb-3">
                <div className="w-2 h-2 bg-indigo-400 rounded-full animate-pulse"></div>
                <h4 className="text-xs font-bold text-indigo-300 uppercase tracking-widest">AI Prediction</h4>
              </div>
              <p className="text-sm text-indigo-100 leading-relaxed">
                Temperature anomaly detected in Battery Cell 3. Predicted thermal throttling in <span className="font-semibold text-white">45 minutes</span>.
              </p>
              <button className="mt-4 w-full py-2 bg-indigo-500/20 hover:bg-indigo-500/30 border border-indigo-500/30 rounded text-xs font-medium text-indigo-300 transition-all flex items-center justify-center gap-2">
                View Diagnostics <ArrowRightIcon className="w-3 h-3" />
              </button>
            </div>

            {/* Anomalies List */}
            <div className="bg-gray-900 rounded-xl p-5 border border-gray-800">
              <div className="flex justify-between items-center mb-4">
                <h3 className="text-sm font-medium text-gray-400 uppercase tracking-wider">Latest Events</h3>
                <span className="px-2 py-0.5 bg-gray-800 rounded text-[10px] text-gray-500">Live</span>
              </div>
              <div className="space-y-3">
                {[
                  { time: '10:42:05', msg: 'Power draw spike detected', type: 'warn' },
                  { time: '10:41:20', msg: 'Sampling rate adjusted', type: 'info' },
                  { time: '10:39:55', msg: 'Cloud sync completed', type: 'success' },
                ].map((log, i) => (
                  <div key={i} className="flex gap-3 items-start text-sm">
                    <span className="font-mono text-xs text-gray-600 mt-0.5">{log.time}</span>
                    <span className={
                      log.type === 'warn' ? 'text-amber-400' :
                      log.type === 'success' ? 'text-green-400' : 'text-blue-400'
                    }>{log.msg}</span>
                  </div>
                ))}
              </div>
            </div>

          </div>
        </div>
      </main>
    </div>
  );
}

function ArrowRightIcon(props: any) {
  return (
    <svg 
      {...props}
      xmlns="http://www.w3.org/2000/svg" 
      fill="none" 
      viewBox="0 0 24 24" 
      strokeWidth={2} 
      stroke="currentColor"
    >
      <path strokeLinecap="round" strokeLinejoin="round" d="M13.5 4.5L21 12m0 0l-7.5 7.5M21 12H3" />
    </svg>
  );
}
