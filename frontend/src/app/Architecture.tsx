import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { 
  Cpu, 
  Download, 
  Box, 
  ArrowRight,
  Wifi,
  ShieldCheck,
  Maximize2,
  Zap
} from 'lucide-react';

export default function Architecture() {
  const navigate = useNavigate();
  const [selectedNode, setSelectedNode] = useState<string>('main-controller');

  // Simple mock data for tree structure
  const systemTree = [
    { id: 'ui', label: 'User Interface', icon: Cpu, color: 'text-purple-400' },
    { id: 'ctrl', label: 'Control Unit', icon: Cpu, color: 'text-blue-400' },
    { id: 'sens', label: 'Sensor Array', icon: Wifi, color: 'text-green-400' },
    { id: 'pwr', label: 'Power Mgmt', icon: Zap, color: 'text-amber-400' },
  ];

  return (
    <div className="h-screen flex flex-col bg-[#F8FAFC] overflow-hidden">
      {/* Header */}
      <header className="px-6 py-4 border-b border-gray-200 bg-white">
        <div className="max-w-[1800px] mx-auto flex justify-between items-center">
          <div className="flex items-center gap-2 cursor-pointer" onClick={() => navigate('/')}>
            <div className="w-8 h-8 bg-gradient-to-br from-blue-600 to-blue-800 rounded-lg flex items-center justify-center">
              <Cpu className="w-5 h-5 text-white" />
            </div>
            <span className="text-lg font-semibold text-gray-900">MedTwin</span>
          </div>
          <nav className="hidden md:flex items-center gap-8">
            <a href="#" onClick={(e) => { e.preventDefault(); navigate('/dashboard'); }} className="text-sm text-gray-600 hover:text-gray-900 transition-colors">Dashboard</a>
            <a href="#" className="text-sm text-gray-600 hover:text-gray-900 transition-colors">Projects</a>
            <a href="#" className="text-sm text-gray-600 hover:text-gray-900 transition-colors">Documentation</a>
            <div className="flex items-center gap-3">
              <div className="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center text-blue-700 font-medium">
                JD
              </div>
            </div>
          </nav>
        </div>
      </header>

      {/* Main Workspace */}
      <div className="flex-1 flex overflow-hidden">
        
        {/* Left Sidebar: System Hierarchy */}
        <div className="w-80 bg-white border-r border-gray-200 flex flex-col shadow-lg">
          <div className="p-6 border-b border-gray-100 flex items-center justify-between bg-gradient-to-r from-gray-50 to-white">
            <h2 className="font-bold text-gray-900 text-sm uppercase tracking-wide">System Hierarchy</h2>
            <div className="flex items-center gap-2 px-3 py-1.5 bg-gradient-to-r from-green-50 to-emerald-50 border border-green-200 rounded-full">
              <div className="w-2 h-2 bg-green-500 rounded-full animate-pulse"></div>
              <span className="text-xs font-bold text-green-700">AI Generated</span>
            </div>
          </div>
          <div className="p-6 space-y-3 overflow-y-auto flex-1">
            {systemTree.map((item, index) => (
              <div 
                key={item.id}
                onClick={() => setSelectedNode(item.id)}
                className={`group flex items-center gap-4 p-4 rounded-xl cursor-pointer transition-all duration-300 hover:scale-105 ${
                  selectedNode === item.id 
                    ? 'bg-gradient-to-r from-blue-50 to-blue-100 border-2 border-blue-300 shadow-lg shadow-blue-500/20' 
                    : 'hover:bg-gray-50 border-2 border-transparent hover:shadow-md'
                }`}
                style={{ animationDelay: `${index * 100}ms` }}
              >
                <div className={`w-12 h-12 rounded-xl flex items-center justify-center transition-all ${
                  selectedNode === item.id 
                    ? 'bg-gradient-to-br from-blue-500 to-blue-600 shadow-lg' 
                    : 'bg-gray-100 group-hover:bg-gray-200'
                }`}>
                  <item.icon className={`w-6 h-6 ${
                    selectedNode === item.id ? 'text-white' : item.color
                  }`} />
                </div>
                <div className="flex-1">
                  <span className="font-semibold text-gray-900 block">{item.label}</span>
                  <span className="text-xs text-gray-500">Component {index + 1}</span>
                </div>
                {selectedNode === item.id && (
                  <div className="w-3 h-3 bg-blue-500 rounded-full animate-pulse"></div>
                )}
              </div>
            ))}
          </div>
          <div className="p-6 border-t border-gray-200 bg-gradient-to-r from-gray-50 to-white">
            <button className="w-full flex items-center justify-center gap-3 text-sm font-semibold text-gray-700 bg-white border-2 border-gray-300 py-3 rounded-xl hover:border-blue-400 hover:text-blue-700 transition-all duration-300 hover:shadow-md">
              <Download className="w-5 h-5" />
              Export Architecture
            </button>
          </div>
        </div>

        {/* Center: Graph Canvas */}
        <div className="flex-1 bg-gradient-to-br from-slate-50 via-blue-50/30 to-purple-50/30 relative overflow-hidden flex items-center justify-center">
          
          {/* Toolbar */}
          <div className="absolute top-6 left-6 flex flex-col gap-3 bg-white/90 backdrop-blur-sm p-2 rounded-xl shadow-lg border border-gray-200/50 z-10">
            <button className="p-3 hover:bg-blue-50 rounded-lg text-gray-600 hover:text-blue-600 transition-all duration-300 group" title="Auto-Layout">
              <Box className="w-5 h-5 group-hover:scale-110 transition-transform" />
            </button>
            <button className="p-3 hover:bg-blue-50 rounded-lg text-gray-600 hover:text-blue-600 transition-all duration-300 group" title="Zoom Fit">
              <Maximize2 className="w-5 h-5 group-hover:scale-110 transition-transform" />
            </button>
          </div>

          {/* Graph Visualization */}
          <div className="relative w-full h-full p-20 flex items-center justify-center">
            {/* Animated Background Grid */}
            <div className="absolute inset-0 bg-[linear-gradient(rgba(59,130,246,0.03)_1px,transparent_1px),linear-gradient(90deg,rgba(59,130,246,0.03)_1px,transparent_1px)] bg-[length:40px_40px] opacity-50"></div>
            
            {/* Connecting Lines (Enhanced SVG) */}
            <svg className="absolute inset-0 w-full h-full pointer-events-none">
              <defs>
                <linearGradient id="connectionGradient" x1="0%" y1="0%" x2="100%" y2="0%">
                  <stop offset="0%" stopColor="#3B82F6" stopOpacity="0.3"/>
                  <stop offset="50%" stopColor="#8B5CF6" stopOpacity="0.5"/>
                  <stop offset="100%" stopColor="#3B82F6" stopOpacity="0.3"/>
                </linearGradient>
              </defs>
              <path d="M 400 300 L 600 300" stroke="url(#connectionGradient)" strokeWidth="3" strokeDasharray="8 4" className="animate-pulse" />
              <path d="M 600 300 L 800 200" stroke="url(#connectionGradient)" strokeWidth="3" />
              <path d="M 600 300 L 800 400" stroke="url(#connectionGradient)" strokeWidth="3" />
            </svg>

            <div className="flex gap-24 items-center">
              {/* Node 1 - Enhanced */}
              <div className="w-56 bg-white/90 backdrop-blur-sm rounded-2xl shadow-2xl border border-gray-200/50 p-6 relative group hover:border-blue-400 transition-all duration-500 hover:scale-105 hover:-translate-y-2">
                <div className="absolute -top-4 left-6 px-4 py-2 bg-gradient-to-r from-blue-500 to-blue-600 text-white text-xs font-bold rounded-lg uppercase shadow-lg">
                  Main Controller
                </div>
                <div className="flex items-center gap-4 mb-4 mt-2">
                  <div className="w-14 h-14 bg-gradient-to-br from-blue-500 to-blue-600 rounded-2xl flex items-center justify-center shadow-lg">
                    <Cpu className="w-8 h-8 text-white" />
                  </div>
                  <div>
                    <div className="text-lg font-bold text-gray-900">STM32H7</div>
                    <div className="text-sm text-gray-500">ARM Cortex-M7 @ 480MHz</div>
                  </div>
                </div>
                <div className="space-y-3">
                  <div className="flex justify-between text-sm">
                    <span className="text-gray-600">CPU Load</span>
                    <span className="font-mono text-gray-900 font-semibold">12%</span>
                  </div>
                  <div className="w-full bg-gray-200 h-2 rounded-full overflow-hidden">
                    <div className="w-[12%] h-full bg-gradient-to-r from-blue-500 to-blue-600 rounded-full"></div>
                  </div>
                  <div className="flex gap-2 mt-4">
                    <span className="px-2 py-1 bg-green-100 text-green-700 text-xs rounded-full font-medium">Active</span>
                    <span className="px-2 py-1 bg-blue-100 text-blue-700 text-xs rounded-full font-medium">Real-time</span>
                  </div>
                </div>
              </div>

              {/* Node 2 - Enhanced */}
              <div className="w-56 bg-white/90 backdrop-blur-sm rounded-2xl shadow-2xl border border-gray-200/50 p-6 relative group hover:border-purple-400 transition-all duration-500 hover:scale-105 hover:-translate-y-2">
                <div className="absolute -top-4 left-6 px-4 py-2 bg-gradient-to-r from-purple-500 to-purple-600 text-white text-xs font-bold rounded-lg uppercase shadow-lg">
                  Security Module
                </div>
                <div className="flex items-center gap-4 mb-4 mt-2">
                  <div className="w-14 h-14 bg-gradient-to-br from-purple-500 to-purple-600 rounded-2xl flex items-center justify-center shadow-lg">
                    <ShieldCheck className="w-8 h-8 text-white" />
                  </div>
                  <div>
                    <div className="text-lg font-bold text-gray-900">TPM 2.0</div>
                    <div className="text-sm text-gray-500">Hardware Security Root</div>
                  </div>
                </div>
                <div className="flex flex-wrap gap-2 mt-4">
                  <span className="px-3 py-1 bg-green-100 text-green-700 text-xs rounded-full font-medium border border-green-200">
                    🔒 Encrypted
                  </span>
                  <span className="px-3 py-1 bg-blue-100 text-blue-700 text-xs rounded-full font-medium border border-blue-200">
                    🛡️ Verified
                  </span>
                </div>
              </div>
            </div>
          </div>

          <div className="absolute bottom-8 right-8">
            <button 
              onClick={() => navigate('/dashboard')}
              className="group flex items-center gap-3 bg-gradient-to-r from-blue-600 via-blue-700 to-purple-700 text-white px-8 py-4 rounded-2xl shadow-2xl shadow-blue-500/30 hover:shadow-3xl hover:shadow-blue-500/50 transition-all duration-500 font-bold text-lg hover:scale-105 hover:-translate-y-1">
              <svg className="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" />
              </svg>
              <span>Launch Digital Twin</span>
              <ArrowRight className="w-6 h-6 group-hover:translate-x-2 transition-transform duration-300" />
            </button>
          </div>
        </div>

        {/* Right Sidebar: Properties */}
        <div className="w-72 bg-white border-l border-gray-200 p-6">
          <h3 className="font-semibold text-gray-900 mb-6">Component Properties</h3>
          
          <div className="space-y-6">
            <div>
              <label className="text-xs font-semibold text-gray-500 uppercase block mb-2">Specifications</label>
              <div className="space-y-2">
                <div className="flex justify-between text-sm py-1 border-b border-gray-100">
                  <span className="text-gray-600">Clock</span>
                  <span className="font-mono text-gray-900">480 MHz</span>
                </div>
                <div className="flex justify-between text-sm py-1 border-b border-gray-100">
                  <span className="text-gray-600">Flash</span>
                  <span className="font-mono text-gray-900">2 MB</span>
                </div>
                <div className="flex justify-between text-sm py-1 border-b border-gray-100">
                  <span className="text-gray-600">RAM</span>
                  <span className="font-mono text-gray-900">1 MB</span>
                </div>
              </div>
            </div>

            <div>
              <label className="text-xs font-semibold text-gray-500 uppercase block mb-2">Interfaces</label>
              <div className="flex flex-wrap gap-2">
                {['SPI', 'I2C', 'UART', 'CAN-FD', 'ETH'].map(tag => (
                  <span key={tag} className="px-2 py-1 bg-gray-100 text-gray-600 rounded text-xs font-medium">
                    {tag}
                  </span>
                ))}
              </div>
            </div>

            <div className="bg-amber-50 p-3 rounded-lg border border-amber-100">
              <div className="flex gap-2">
                <Box className="w-4 h-4 text-amber-600 mt-0.5" />
                <div>
                  <h4 className="text-xs font-bold text-amber-800 mb-1">Architecture Note</h4>
                  <p className="text-xs text-amber-700 leading-relaxed">
                    Consider adding a redundant power supervisor for medical safety compliance (IEC 60601-1).
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}