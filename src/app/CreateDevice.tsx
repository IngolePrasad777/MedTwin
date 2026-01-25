import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { 
  Cpu, 
  ChevronDown, 
  Check, 
  Zap, 
  Gauge, 
  Code2, 
  AlertCircle
} from 'lucide-react';

export default function CreateDevice() {
  const navigate = useNavigate();
  const [deviceType, setDeviceType] = useState<string>('');
  const [powerSource, setPowerSource] = useState<string>('');
  const [standards, setStandards] = useState<string[]>([]);
  const [isAnalyzing, setIsAnalyzing] = useState<boolean>(false);

  const toggleStandard = (s: string) => {
    setStandards((prev: string[]) => prev.includes(s) 
      ? prev.filter((i: string) => i !== s) 
      : [...prev, s]
    );
  };

  // Simulate AI analysis when device type changes
  const handleDeviceTypeChange = (type: string) => {
    setDeviceType(type);
    setIsAnalyzing(true);
    setTimeout(() => setIsAnalyzing(false), 1500);
  };

  return (
    <div className="min-h-screen bg-[#F8FAFC]">
      {/* Header */}
      <header className="px-6 py-4 border-b border-gray-200 bg-white">
        <div className="max-w-7xl mx-auto flex justify-between items-center">
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

      {/* Main Content */}
      <main className="max-w-7xl mx-auto px-6 py-8">
        {/* Breadcrumb */}
        <div className="flex items-center gap-2 text-sm text-gray-600 mb-6">
          <a href="#" onClick={(e) => { e.preventDefault(); navigate('/'); }} className="hover:text-gray-900">Home</a>
          <ChevronDown className="w-4 h-4 rotate-[-90deg]" />
          <span className="text-gray-900">Create Device</span>
        </div>

        <div className="flex justify-between items-start mb-10">
          <div>
            <h1 className="text-4xl font-bold text-gray-900 mb-3">Define Requirements</h1>
            <p className="text-lg text-gray-600">Specify constraints and parameters for your medical device digital twin.</p>
          </div>
          <button 
            onClick={() => navigate('/architecture')}
            className="group flex items-center gap-3 bg-gradient-to-r from-blue-600 to-purple-600 text-white px-8 py-4 rounded-xl hover:from-blue-700 hover:to-purple-700 transition-all duration-300 shadow-lg shadow-blue-500/25 hover:shadow-xl hover:shadow-blue-500/40 font-semibold">
            <svg className="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" />
            </svg>
            <span>Generate Architecture</span>
            <svg className="w-5 h-5 group-hover:translate-x-1 transition-transform" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 7l5 5m0 0l-5 5m5-5H6" />
            </svg>
          </button>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Left Column: Form */}
          <div className="lg:col-span-2 space-y-6">
            
            {/* Device Type Selection */}
            <div className="bg-white p-8 rounded-2xl border border-gray-200 shadow-sm hover:shadow-md transition-shadow">
              <h3 className="font-bold text-xl text-gray-900 mb-6 flex items-center gap-3">
                <div className="w-10 h-10 bg-gradient-to-br from-blue-500 to-blue-600 rounded-xl flex items-center justify-center">
                  <Gauge className="w-6 h-6 text-white" />
                </div>
                Device Classification
              </h3>
              <div className="grid grid-cols-2 gap-6">
                {[
                  { name: 'Infusion Pump', desc: 'Controlled fluid delivery', icon: '💉' },
                  { name: 'Patient Monitor', desc: 'Vital signs tracking', icon: '📊' },
                  { name: 'Ventilator', desc: 'Respiratory support', icon: '🫁' },
                  { name: 'Diagnostic Handler', desc: 'Sample processing', icon: '🔬' }
                ].map((type) => (
                  <button
                    key={type.name}
                    onClick={() => handleDeviceTypeChange(type.name)}
                    className={`p-6 rounded-xl border-2 text-left transition-all duration-300 hover:scale-105 ${
                      deviceType === type.name 
                        ? 'border-blue-500 bg-gradient-to-br from-blue-50 to-blue-100 text-blue-800 shadow-lg shadow-blue-500/20' 
                        : 'border-gray-200 hover:border-blue-300 hover:shadow-md'
                    }`}
                  >
                    <div className="text-2xl mb-3">{type.icon}</div>
                    <div className="font-semibold text-lg mb-2">{type.name}</div>
                    <div className="text-sm text-gray-500 mb-3">{type.desc}</div>
                    <div className="text-xs font-medium text-gray-400 bg-gray-100 px-2 py-1 rounded-full inline-block">
                      Class IIb Medical Device
                    </div>
                  </button>
                ))}
              </div>
            </div>

            {/* Power Constraints */}
            <div className="bg-white p-8 rounded-2xl border border-gray-200 shadow-sm hover:shadow-md transition-shadow">
              <h3 className="font-bold text-xl text-gray-900 mb-6 flex items-center gap-3">
                <div className="w-10 h-10 bg-gradient-to-br from-amber-500 to-orange-500 rounded-xl flex items-center justify-center">
                  <Zap className="w-6 h-6 text-white" />
                </div>
                Power Architecture
              </h3>
              <div className="flex flex-wrap gap-4">
                {[
                  { name: 'Mains Only', desc: 'AC powered', color: 'from-green-500 to-green-600' },
                  { name: 'Battery Backup', desc: 'Hybrid power', color: 'from-blue-500 to-blue-600' },
                  { name: 'Portable (Battery Only)', desc: 'Mobile device', color: 'from-purple-500 to-purple-600' }
                ].map((p) => (
                  <button
                    key={p.name}
                    onClick={() => setPowerSource(p.name)}
                    className={`px-6 py-4 rounded-xl text-sm border-2 transition-all duration-300 hover:scale-105 flex-1 min-w-[200px] ${
                      powerSource === p.name
                        ? `bg-gradient-to-r ${p.color} text-white border-transparent shadow-lg`
                        : 'border-gray-200 text-gray-600 hover:border-gray-300 hover:shadow-md bg-white'
                    }`}
                  >
                    <div className="font-semibold">{p.name}</div>
                    <div className={`text-xs mt-1 ${powerSource === p.name ? 'text-white/80' : 'text-gray-400'}`}>
                      {p.desc}
                    </div>
                  </button>
                ))}
              </div>
            </div>

            {/* Standards Compliance */}
            <div className="bg-white p-8 rounded-2xl border border-gray-200 shadow-sm hover:shadow-md transition-shadow">
              <h3 className="font-bold text-xl text-gray-900 mb-6 flex items-center gap-3">
                <div className="w-10 h-10 bg-gradient-to-br from-purple-500 to-purple-600 rounded-xl flex items-center justify-center">
                  <Code2 className="w-6 h-6 text-white" />
                </div>
                Compliance Standards
              </h3>
              <div className="space-y-4">
                {[
                  { name: 'IEC 60601-1 (General Safety)', desc: 'Basic safety and essential performance', critical: true },
                  { name: 'IEC 62304 (Software Life Cycle)', desc: 'Medical device software processes', critical: true },
                  { name: 'ISO 14971 (Risk Management)', desc: 'Application of risk management', critical: false }
                ].map((std) => (
                  <div 
                    key={std.name}
                    onClick={() => toggleStandard(std.name)}
                    className={`flex items-start gap-4 p-4 rounded-xl border-2 cursor-pointer transition-all duration-300 hover:scale-[1.02] ${
                      standards.includes(std.name)
                        ? 'border-purple-500 bg-gradient-to-r from-purple-50 to-purple-100 shadow-lg shadow-purple-500/20'
                        : 'border-gray-200 hover:border-purple-300 hover:shadow-md bg-white'
                    }`}
                  >
                    <div className={`w-6 h-6 rounded-lg border-2 flex items-center justify-center transition-all ${
                      standards.includes(std.name) 
                        ? 'bg-purple-500 border-purple-500' 
                        : 'border-gray-300 bg-white'
                    }`}>
                      {standards.includes(std.name) && <Check className="w-4 h-4 text-white" />}
                    </div>
                    <div className="flex-1">
                      <div className="flex items-center gap-2 mb-1">
                        <span className="font-semibold text-gray-900">{std.name}</span>
                        {std.critical && (
                          <span className="px-2 py-0.5 bg-red-100 text-red-700 text-xs font-medium rounded-full">
                            Required
                          </span>
                        )}
                      </div>
                      <p className="text-sm text-gray-600">{std.desc}</p>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>

          {/* Right Column: AI Assistant */}
          <div className="bg-gradient-to-br from-gray-900 to-gray-800 text-white p-6 rounded-xl shadow-lg h-fit sticky top-6">
            <div className="flex items-center gap-2 mb-6 border-b border-gray-700 pb-4">
              <div className="w-2 h-2 bg-green-400 rounded-full animate-pulse"></div>
              <span className="font-mono text-sm tracking-wide text-gray-300">SYSTEM ARCHITECT AI</span>
            </div>
            
            <div className="space-y-6">
              <div className="bg-gray-800/50 p-4 rounded-lg border border-gray-700/50">
                <div className="flex items-start gap-3">
                  <AlertCircle className="w-5 h-5 text-blue-400 mt-0.5" />
                  <div>
                    {isAnalyzing ? (
                      <div className="flex items-center gap-2">
                        <div className="w-4 h-4 border-2 border-blue-400 border-t-transparent rounded-full animate-spin"></div>
                        <p className="text-sm text-gray-300">Analyzing requirements...</p>
                      </div>
                    ) : (
                      <p className="text-sm text-gray-200 leading-relaxed">
                        "Based on <strong>{deviceType || 'selection'}</strong>, I recommend a dual-processor architecture to separate control logic from UI rendering (IEC 62304 segregation)."
                      </p>
                    )}
                  </div>
                </div>
              </div>

              <div className="space-y-2">
                <div className="flex justify-between text-xs text-gray-400 uppercase tracking-wider">
                  <span>Confidence Score</span>
                  <span>{isAnalyzing ? '--' : '98%'}</span>
                </div>
                <div className="h-1.5 bg-gray-700 rounded-full overflow-hidden">
                  <div className={`h-full bg-gradient-to-r from-blue-500 to-purple-500 transition-all duration-1000 ${
                    isAnalyzing ? 'w-0' : 'w-[98%]'
                  }`}></div>
                </div>
              </div>

              <div className="pt-4 border-t border-gray-700">
                <h4 className="text-xs font-semibold text-gray-400 uppercase mb-3">Suggested Components</h4>
                <div className="flex flex-wrap gap-2">
                  <span className="px-2 py-1 bg-gray-700 rounded text-xs text-gray-300">STM32H7 (Control)</span>
                  <span className="px-2 py-1 bg-gray-700 rounded text-xs text-gray-300">i.MX 8M (UI)</span>
                  <span className="px-2 py-1 bg-gray-700 rounded text-xs text-gray-300">FreeRTOS</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}