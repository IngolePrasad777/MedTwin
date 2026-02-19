import { FileText, Cpu, Activity, Target } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

export default function LandingPage() {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-blue-50">
      {/* Header */}
      <header className="px-6 py-4 border-b border-gray-200 bg-white/80 backdrop-blur-sm">
        <div className="max-w-7xl mx-auto flex justify-between items-center">
          <div className="flex items-center gap-2 cursor-pointer" onClick={() => navigate('/')}>
            <div className="w-8 h-8 bg-gradient-to-br from-blue-600 to-blue-800 rounded-lg flex items-center justify-center">
              <Cpu className="w-5 h-5 text-white" />
            </div>
            <span className="text-lg font-semibold text-gray-900">MedTwin</span>
          </div>
          <nav className="hidden md:flex items-center gap-8">
            <a href="#" className="text-sm text-gray-600 hover:text-gray-900 transition-colors">Solutions</a>
            <a href="#" className="text-sm text-gray-600 hover:text-gray-900 transition-colors">Documentation</a>
            <a href="#" className="text-sm text-gray-600 hover:text-gray-900 transition-colors">Enterprise</a>
            <button 
              onClick={() => navigate('/dashboard')}
              className="text-sm px-4 py-2 text-blue-600 hover:bg-blue-50 rounded-lg transition-colors">
              Sign In
            </button>
          </nav>
        </div>
      </header>

      {/* Hero Section */}
      <main className="max-w-7xl mx-auto px-6 py-20 md:py-32">
        <div className="text-center max-w-6xl mx-auto">
          {/* Badge */}
          <div className="inline-flex items-center gap-2 px-5 py-2.5 bg-gradient-to-r from-blue-50 to-purple-50 border border-blue-200/50 text-blue-700 rounded-full text-sm mb-8 shadow-sm">
            <div className="w-2 h-2 bg-gradient-to-r from-blue-500 to-purple-500 rounded-full animate-pulse"></div>
            <span className="font-medium">Autonomous System Design Platform</span>
            <div className="w-1 h-1 bg-blue-400 rounded-full"></div>
            <span className="text-xs font-semibold text-purple-600">AI-POWERED</span>
          </div>

          {/* Main Title */}
          <h1 className="text-5xl md:text-7xl lg:text-8xl font-bold text-gray-900 mb-8 leading-[0.9] tracking-tight">
            Medical Device<br />
            <span className="bg-gradient-to-r from-blue-600 via-purple-600 to-blue-800 bg-clip-text text-transparent">
              Digital Twin
            </span><br />
            Engine
          </h1>

          {/* Subheading */}
          <p className="text-xl md:text-2xl text-gray-600 mb-6 max-w-4xl mx-auto font-light leading-relaxed">
            Transform static requirements into self-evolving system blueprints with AI-powered architecture generation.
          </p>

          {/* Micro-line */}
          <p className="text-base text-gray-500 mb-12 max-w-3xl mx-auto leading-relaxed">
            Design, simulate, and validate medical devices before hardware exists. Powered by generative AI and physics-informed neural networks for regulatory-compliant development.
          </p>

          {/* CTA Button */}
          <div className="flex flex-col sm:flex-row items-center justify-center gap-4 mb-8">
            <button 
              onClick={() => navigate('/create')}
              className="group inline-flex items-center gap-3 px-10 py-5 bg-gradient-to-r from-blue-600 via-blue-700 to-purple-700 text-white rounded-2xl font-semibold text-lg shadow-2xl shadow-blue-500/30 hover:shadow-3xl hover:shadow-blue-500/50 transition-all duration-500 hover:scale-105 hover:-translate-y-1">
              <span>Start Building</span>
              <svg 
                className="w-6 h-6 group-hover:translate-x-2 transition-transform duration-300" 
                fill="none" 
                viewBox="0 0 24 24" 
                stroke="currentColor"
              >
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 7l5 5m0 0l-5 5m5-5H6" />
              </svg>
            </button>
            
            <button 
              onClick={() => navigate('/dashboard')}
              className="inline-flex items-center gap-2 px-8 py-4 bg-white border-2 border-gray-200 text-gray-700 rounded-xl font-medium hover:border-blue-300 hover:text-blue-700 transition-all duration-300 hover:shadow-lg">
              <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M14.828 14.828a4 4 0 01-5.656 0M9 10h1m4 0h1m-6 4h8m-9-4h10a2 2 0 012 2v8a2 2 0 01-2 2H7a2 2 0 01-2-2v-8a2 2 0 012-2z" />
              </svg>
              View Demo
            </button>
          </div>

          {/* Secondary Text */}
          <p className="text-sm text-gray-500">
            No credit card required • Enterprise-grade security • ISO 13485 Compliant
          </p>
        </div>

        {/* Process Flow */}
        <div className="mt-24 md:mt-32">
          <div className="bg-white/70 backdrop-blur-xl rounded-3xl shadow-2xl shadow-gray-200/50 p-8 md:p-16 border border-gray-100/50">
            <div className="text-center mb-12">
              <h2 className="text-3xl md:text-4xl font-bold text-gray-900 mb-4">How It Works</h2>
              <p className="text-lg text-gray-600 max-w-2xl mx-auto">Four-step process from concept to validated digital twin</p>
            </div>
            
            <div className="grid grid-cols-1 md:grid-cols-4 gap-8 md:gap-6">
              {/* Step 1: Requirements */}
              <div className="relative flex flex-col items-center text-center group">
                <div className="w-20 h-20 bg-gradient-to-br from-blue-500 via-blue-600 to-blue-700 rounded-3xl flex items-center justify-center mb-6 shadow-xl shadow-blue-500/30 group-hover:scale-110 group-hover:rotate-3 transition-all duration-500">
                  <FileText className="w-10 h-10 text-white" />
                </div>
                <div className="absolute -top-2 -right-2 w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center">
                  <span className="text-sm font-bold text-blue-600">1</span>
                </div>
                <h3 className="font-bold text-lg text-gray-900 mb-3">Requirements</h3>
                <p className="text-sm text-gray-600 leading-relaxed">Ingest & structure constraints with AI-powered requirement analysis</p>
                
                {/* Arrow */}
                <div className="hidden md:block absolute top-10 -right-12 text-blue-300">
                  <svg className="w-24 h-8" fill="none" viewBox="0 0 96 32" xmlns="http://www.w3.org/2000/svg">
                    <path d="M2 16h88m0 0l-12-12m12 12l-12 12" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" strokeDasharray="4 4"/>
                  </svg>
                </div>
              </div>

              {/* Step 2: Digital Twin */}
              <div className="relative flex flex-col items-center text-center group">
                <div className="w-20 h-20 bg-gradient-to-br from-blue-600 via-purple-600 to-purple-700 rounded-3xl flex items-center justify-center mb-6 shadow-xl shadow-purple-500/30 group-hover:scale-110 group-hover:rotate-3 transition-all duration-500">
                  <Cpu className="w-10 h-10 text-white" />
                </div>
                <div className="absolute -top-2 -right-2 w-8 h-8 bg-purple-100 rounded-full flex items-center justify-center">
                  <span className="text-sm font-bold text-purple-600">2</span>
                </div>
                <h3 className="font-bold text-lg text-gray-900 mb-3">Digital Twin</h3>
                <p className="text-sm text-gray-600 leading-relaxed">Generate living system model with physics-informed neural networks</p>
                
                {/* Arrow */}
                <div className="hidden md:block absolute top-10 -right-12 text-purple-300">
                  <svg className="w-24 h-8" fill="none" viewBox="0 0 96 32" xmlns="http://www.w3.org/2000/svg">
                    <path d="M2 16h88m0 0l-12-12m12 12l-12 12" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" strokeDasharray="4 4"/>
                  </svg>
                </div>
              </div>

              {/* Step 3: Simulation */}
              <div className="relative flex flex-col items-center text-center group">
                <div className="w-20 h-20 bg-gradient-to-br from-purple-600 via-indigo-600 to-indigo-700 rounded-3xl flex items-center justify-center mb-6 shadow-xl shadow-indigo-500/30 group-hover:scale-110 group-hover:rotate-3 transition-all duration-500">
                  <Activity className="w-10 h-10 text-white" />
                </div>
                <div className="absolute -top-2 -right-2 w-8 h-8 bg-indigo-100 rounded-full flex items-center justify-center">
                  <span className="text-sm font-bold text-indigo-600">3</span>
                </div>
                <h3 className="font-bold text-lg text-gray-900 mb-3">Simulation</h3>
                <p className="text-sm text-gray-600 leading-relaxed">Run behavior & stress scenarios with real-time validation</p>
                
                {/* Arrow */}
                <div className="hidden md:block absolute top-10 -right-12 text-indigo-300">
                  <svg className="w-24 h-8" fill="none" viewBox="0 0 96 32" xmlns="http://www.w3.org/2000/svg">
                    <path d="M2 16h88m0 0l-12-12m12 12l-12 12" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" strokeDasharray="4 4"/>
                  </svg>
                </div>
              </div>

              {/* Step 4: Decisions */}
              <div className="flex flex-col items-center text-center group">
                <div className="w-20 h-20 bg-gradient-to-br from-indigo-600 via-blue-700 to-blue-800 rounded-3xl flex items-center justify-center mb-6 shadow-xl shadow-blue-700/30 group-hover:scale-110 group-hover:rotate-3 transition-all duration-500">
                  <Target className="w-10 h-10 text-white" />
                </div>
                <div className="absolute -top-2 -right-2 w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center">
                  <span className="text-sm font-bold text-blue-600">4</span>
                </div>
                <h3 className="font-bold text-lg text-gray-900 mb-3">Optimization</h3>
                <p className="text-sm text-gray-600 leading-relaxed">AI-driven architecture optimization with compliance validation</p>
              </div>
            </div>
          </div>
        </div>

        {/* Trust Indicators */}
        <div className="mt-20 flex flex-col items-center">
          <p className="text-gray-400 text-sm font-medium mb-8">Trusted by leading healthcare enterprises worldwide</p>
          <div className="flex flex-wrap justify-center items-center gap-12 opacity-60">
            <div className="flex items-center gap-2">
              <div className="w-8 h-8 bg-green-100 rounded-lg flex items-center justify-center">
                <svg className="w-5 h-5 text-green-600" fill="currentColor" viewBox="0 0 20 20">
                  <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
                </svg>
              </div>
              <span className="text-gray-600 font-semibold">ISO 13485</span>
            </div>
            <div className="flex items-center gap-2">
              <div className="w-8 h-8 bg-blue-100 rounded-lg flex items-center justify-center">
                <svg className="w-5 h-5 text-blue-600" fill="currentColor" viewBox="0 0 20 20">
                  <path fillRule="evenodd" d="M2.166 4.999A11.954 11.954 0 0010 1.944 11.954 11.954 0 0017.834 5c.11.65.166 1.32.166 2.001 0 5.225-3.34 9.67-8 11.317C5.34 16.67 2 12.225 2 7c0-.682.057-1.35.166-2.001zm11.541 3.708a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
                </svg>
              </div>
              <span className="text-gray-600 font-semibold">FDA Compliant</span>
            </div>
            <div className="flex items-center gap-2">
              <div className="w-8 h-8 bg-purple-100 rounded-lg flex items-center justify-center">
                <svg className="w-5 h-5 text-purple-600" fill="currentColor" viewBox="0 0 20 20">
                  <path fillRule="evenodd" d="M18 8a6 6 0 01-7.743 5.743L10 14l-0.257-0.257A6 6 0 1118 8zM2 8a6 6 0 1112 0 6 6 0 01-12 0z" clipRule="evenodd" />
                </svg>
              </div>
              <span className="text-gray-600 font-semibold">SOC 2 Type II</span>
            </div>
            <div className="flex items-center gap-2">
              <div className="w-8 h-8 bg-amber-100 rounded-lg flex items-center justify-center">
                <svg className="w-5 h-5 text-amber-600" fill="currentColor" viewBox="0 0 20 20">
                  <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                </svg>
              </div>
              <span className="text-gray-600 font-semibold">HIPAA Ready</span>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}
