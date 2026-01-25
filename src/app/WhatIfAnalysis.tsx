import { useNavigate } from 'react-router-dom';
import { 
  ArrowLeft, 
  Cpu, 
  Zap, 
  Wind, 
  Thermometer, 
  Play, 
  RotateCcw,
  Sparkles,
  TrendingDown,
  TrendingUp, 
  Settings,
  Battery,
  AlertTriangle,
  CheckCircle2,
  Info,
  Target,
  Activity,
  BarChart3,
  LineChart,
  PieChart,
  Download,
  Share2,
  Bookmark,
  Clock,
  Gauge
} from 'lucide-react';
import { useState, useEffect } from 'react';

interface SimulationParams {
  batterySize: number;
  samplingRate: number;
  airflowTarget: number;
  processingPower: number;
  thermalThreshold: number;
  powerMode: 'eco' | 'balanced' | 'performance';
}

interface SimulationResults {
  batteryLife: number;
  thermalLoad: number;
  efficiency: number;
  reliability: number;
  costImpact: number;
  riskScore: number;
  complianceScore: number;
}

interface ScenarioPreset {
  name: string;
  description: string;
  params: SimulationParams;
  color: string;
}

export default function WhatIfAnalysis() {
  const navigate = useNavigate();
  
  // Enhanced Simulation State
  const [params, setParams] = useState<SimulationParams>({
    batterySize: 5000, // mAh
    samplingRate: 100, // Hz
    airflowTarget: 45, // L/min
    processingPower: 75, // %
    thermalThreshold: 60, // °C
    powerMode: 'balanced'
  });

  const [simResults, setSimResults] = useState<SimulationResults>({
    batteryLife: 14.5,
    thermalLoad: 42,
    efficiency: 88,
    reliability: 94,
    costImpact: 0,
    riskScore: 15,
    complianceScore: 98
  });

  const [isSimulating, setIsSimulating] = useState(false);
  const [selectedChart, setSelectedChart] = useState<'performance' | 'thermal' | 'battery' | 'risk'>('performance');
  const [simulationHistory, setSimulationHistory] = useState<SimulationResults[]>([]);
  const [activeScenario, setActiveScenario] = useState<string>('custom');

  // Predefined scenarios
  const scenarios: ScenarioPreset[] = [
    {
      name: 'Emergency Mode',
      description: 'Maximum performance for critical situations',
      params: { ...params, samplingRate: 200, processingPower: 100, powerMode: 'performance' },
      color: 'bg-red-500'
    },
    {
      name: 'Extended Battery',
      description: 'Optimized for longest battery life',
      params: { ...params, samplingRate: 50, processingPower: 40, powerMode: 'eco' },
      color: 'bg-green-500'
    },
    {
      name: 'Balanced Clinical',
      description: 'Optimal balance for routine clinical use',
      params: { ...params, samplingRate: 100, processingPower: 75, powerMode: 'balanced' },
      color: 'bg-blue-500'
    },
    {
      name: 'High Precision',
      description: 'Maximum accuracy for complex procedures',
      params: { ...params, samplingRate: 150, airflowTarget: 50, processingPower: 90 },
      color: 'bg-purple-500'
    }
  ];

  // Advanced physics simulation
  const runAdvancedSimulation = (inputParams: SimulationParams): SimulationResults => {
    const { batterySize, samplingRate, airflowTarget, processingPower, thermalThreshold, powerMode } = inputParams;
    
    // Power consumption model
    const basePower = 2.5; // Watts
    const samplingPower = (samplingRate / 100) * 1.2;
    const processingPowerConsumption = (processingPower / 100) * 2.0;
    const airflowPower = (airflowTarget / 50) * 1.5;
    const modeMultiplier = powerMode === 'eco' ? 0.7 : powerMode === 'performance' ? 1.3 : 1.0;
    
    const totalPower = (basePower + samplingPower + processingPowerConsumption + airflowPower) * modeMultiplier;
    const batteryLife = (batterySize / 1000) * (3.7 / totalPower); // Hours
    
    // Thermal model
    const thermalGeneration = totalPower * 0.3; // 30% of power becomes heat
    const thermalDissipation = Math.min(thermalGeneration * 0.8, 2.0);
    const thermalLoad = 25 + (thermalGeneration - thermalDissipation) * 15;
    
    // Efficiency model
    const thermalEfficiencyLoss = Math.max(0, (thermalLoad - 40) * 0.5);
    const powerEfficiencyLoss = Math.max(0, (processingPower - 80) * 0.2);
    const efficiency = Math.max(60, 98 - thermalEfficiencyLoss - powerEfficiencyLoss);
    
    // Reliability model (decreases with higher thermal load and processing power)
    const thermalReliabilityImpact = Math.max(0, (thermalLoad - 50) * 0.8);
    const powerReliabilityImpact = Math.max(0, (processingPower - 85) * 0.3);
    const reliability = Math.max(75, 99 - thermalReliabilityImpact - powerReliabilityImpact);
    
    // Cost impact (relative to baseline)
    const batteryCost = ((batterySize - 4000) / 1000) * 25;
    const processingCost = ((processingPower - 50) / 50) * 15;
    const costImpact = batteryCost + processingCost;
    
    // Risk assessment
    const thermalRisk = thermalLoad > thermalThreshold ? (thermalLoad - thermalThreshold) * 2 : 0;
    const powerRisk = processingPower > 90 ? (processingPower - 90) * 1.5 : 0;
    const batteryRisk = batteryLife < 8 ? (8 - batteryLife) * 3 : 0;
    const riskScore = Math.min(100, thermalRisk + powerRisk + batteryRisk);
    
    // Compliance score (based on medical device standards)
    const thermalCompliance = thermalLoad < thermalThreshold ? 100 : Math.max(60, 100 - (thermalLoad - thermalThreshold) * 5);
    const reliabilityCompliance = reliability > 95 ? 100 : reliability;
    const complianceScore = Math.min(thermalCompliance, reliabilityCompliance);
    
    return {
      batteryLife: Math.max(0.1, batteryLife),
      thermalLoad: Math.min(85, thermalLoad),
      efficiency: Math.max(60, efficiency),
      reliability: Math.max(75, reliability),
      costImpact,
      riskScore: Math.max(0, riskScore),
      complianceScore: Math.max(60, complianceScore)
    };
  };

  const runSimulation = () => {
    setIsSimulating(true);
    setTimeout(() => {
      const results = runAdvancedSimulation(params);
      setSimResults(results);
      setSimulationHistory(prev => [...prev.slice(-9), results]); // Keep last 10 results
      setIsSimulating(false);
    }, 1200);
  };

  const applyScenario = (scenario: ScenarioPreset) => {
    setParams(scenario.params);
    setActiveScenario(scenario.name);
    // Auto-run simulation
    setTimeout(() => {
      const results = runAdvancedSimulation(scenario.params);
      setSimResults(results);
      setSimulationHistory(prev => [...prev.slice(-9), results]);
    }, 100);
  };

  const applyAIOptimization = () => {
    // AI-optimized parameters based on current constraints
    const optimizedParams: SimulationParams = {
      batterySize: 4800,
      samplingRate: 85,
      airflowTarget: 42,
      processingPower: 70,
      thermalThreshold: params.thermalThreshold,
      powerMode: 'balanced'
    };
    
    setParams(optimizedParams);
    setActiveScenario('AI Optimized');
    
    setTimeout(() => {
      const results = runAdvancedSimulation(optimizedParams);
      setSimResults(results);
      setSimulationHistory(prev => [...prev.slice(-9), results]);
    }, 100);
  };

  // Auto-update results when parameters change
  useEffect(() => {
    const debounceTimer = setTimeout(() => {
      if (activeScenario === 'custom') {
        const results = runAdvancedSimulation(params);
        setSimResults(results);
      }
    }, 300);
    
    return () => clearTimeout(debounceTimer);
  }, [params, activeScenario]);

  // Generate chart data based on selected chart type
  const generateChartData = () => {
    const hours = Array.from({length: 24}, (_, i) => i);
    
    switch (selectedChart) {
      case 'performance':
        return hours.map(h => ({
          hour: h,
          value: Math.max(10, simResults.efficiency - (h * (100 - simResults.efficiency) / 30) + Math.sin(h * 0.5) * 5),
          label: 'Efficiency %'
        }));
      case 'thermal':
        return hours.map(h => ({
          hour: h,
          value: Math.min(80, simResults.thermalLoad + (h * 0.8) + Math.sin(h * 0.3) * 3),
          label: 'Temperature °C'
        }));
      case 'battery':
        return hours.map(h => ({
          hour: h,
          value: Math.max(0, 100 - (h / simResults.batteryLife) * 100),
          label: 'Battery %'
        }));
      case 'risk':
        return hours.map(h => ({
          hour: h,
          value: Math.min(100, simResults.riskScore + (h * 1.2) + Math.random() * 5),
          label: 'Risk Score'
        }));
      default:
        return [];
    }
  };

  const chartData = generateChartData();

  return (
    <div className="min-h-screen bg-gray-900 text-white font-sans">
      
      {/* Enhanced Header */}
      <header className="px-6 py-4 border-b border-gray-800 bg-gray-950">
        <div className="max-w-[1800px] mx-auto flex justify-between items-center">
          <div className="flex items-center gap-4">
            <div className="flex items-center gap-2 cursor-pointer" onClick={() => navigate('/')}>
              <div className="w-8 h-8 bg-gradient-to-br from-blue-600 to-blue-800 rounded-lg flex items-center justify-center">
                <Cpu className="w-5 h-5 text-white" />
              </div>
              <span className="text-lg font-semibold text-white">MedTwin</span>
            </div>
            <div className="px-3 py-1 bg-purple-500/10 border border-purple-500/20 rounded-full">
              <span className="text-xs font-medium text-purple-400 uppercase tracking-wide">Advanced Simulation</span>
            </div>
            <div className="flex items-center gap-2 px-3 py-1 bg-gray-800 rounded-full">
              <Clock className="w-3 h-3 text-gray-400" />
              <span className="text-xs text-gray-400">Real-time Analysis</span>
            </div>
          </div>
          
          <div className="flex items-center gap-4">
            <button className="p-2 text-gray-400 hover:text-white hover:bg-gray-800 rounded-lg transition-all">
              <Download className="w-4 h-4" />
            </button>
            <button className="p-2 text-gray-400 hover:text-white hover:bg-gray-800 rounded-lg transition-all">
              <Share2 className="w-4 h-4" />
            </button>
            <button className="p-2 text-gray-400 hover:text-white hover:bg-gray-800 rounded-lg transition-all">
              <Bookmark className="w-4 h-4" />
            </button>
            <button 
              onClick={() => navigate('/dashboard')}
              className="text-sm text-gray-400 hover:text-white flex items-center gap-2 transition-colors">
              <ArrowLeft className="w-4 h-4" />
              Back to Dashboard
            </button>
          </div>
        </div>
      </header>

      <main className="max-w-[1800px] mx-auto p-6 md:p-8">
        
        {/* Page Title & Status */}
        <div className="mb-8">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h1 className="text-3xl font-bold mb-2">What-If Analysis Engine</h1>
              <p className="text-gray-400">Advanced multi-parameter simulation with AI-powered optimization</p>
            </div>
            <div className="flex items-center gap-3">
              <div className={`px-3 py-1 rounded-full text-xs font-medium ${
                simResults.riskScore < 20 ? 'bg-green-500/20 text-green-400' :
                simResults.riskScore < 50 ? 'bg-yellow-500/20 text-yellow-400' :
                'bg-red-500/20 text-red-400'
              }`}>
                Risk: {simResults.riskScore.toFixed(0)}%
              </div>
              <div className={`px-3 py-1 rounded-full text-xs font-medium ${
                simResults.complianceScore > 95 ? 'bg-green-500/20 text-green-400' :
                simResults.complianceScore > 85 ? 'bg-yellow-500/20 text-yellow-400' :
                'bg-red-500/20 text-red-400'
              }`}>
                Compliance: {simResults.complianceScore.toFixed(0)}%
              </div>
            </div>
          </div>
        </div>

        <div className="grid grid-cols-1 xl:grid-cols-12 gap-8">
          
          {/* Left Panel: Enhanced Controls */}
          <div className="xl:col-span-4 space-y-6">
            
            {/* Scenario Presets */}
            <div className="bg-gray-800/50 rounded-xl p-6 border border-gray-700">
              <h3 className="text-lg font-semibold mb-4 flex items-center gap-2">
                <Target className="w-5 h-5 text-purple-400" />
                Scenario Presets
              </h3>
              <div className="grid grid-cols-2 gap-3">
                {scenarios.map((scenario) => (
                  <button
                    key={scenario.name}
                    onClick={() => applyScenario(scenario)}
                    className={`p-3 rounded-lg border text-left transition-all ${
                      activeScenario === scenario.name
                        ? 'border-purple-500 bg-purple-500/10'
                        : 'border-gray-600 hover:border-purple-400 hover:bg-gray-700/50'
                    }`}
                  >
                    <div className={`w-3 h-3 rounded-full ${scenario.color} mb-2`}></div>
                    <div className="text-sm font-medium text-white mb-1">{scenario.name}</div>
                    <div className="text-xs text-gray-400">{scenario.description}</div>
                  </button>
                ))}
              </div>
            </div>

            {/* Advanced Parameters */}
            <div className="bg-gray-800/50 rounded-xl p-6 border border-gray-700">
              <h2 className="text-lg font-semibold mb-6 flex items-center gap-2">
                <Settings className="w-5 h-5 text-blue-400" />
                Simulation Parameters
              </h2>

              <div className="space-y-6">
                {/* Battery Capacity */}
                <div className="space-y-3">
                  <div className="flex justify-between text-sm">
                    <span className="text-gray-300 flex items-center gap-2">
                      <Battery className="w-4 h-4 text-green-400" /> Battery Capacity
                    </span>
                    <span className="font-mono text-blue-300">{params.batterySize} mAh</span>
                  </div>
                  <input 
                    type="range" 
                    min="2000" max="10000" step="100"
                    value={params.batterySize}
                    onChange={(e) => {
                      setParams({...params, batterySize: parseInt(e.target.value)});
                      setActiveScenario('custom');
                    }}
                    className="w-full h-2 bg-gray-700 rounded-lg appearance-none cursor-pointer accent-blue-500"
                  />
                  <div className="flex justify-between text-xs text-gray-500">
                    <span>2000</span>
                    <span>10000</span>
                  </div>
                </div>

                {/* Sampling Rate */}
                <div className="space-y-3">
                  <div className="flex justify-between text-sm">
                    <span className="text-gray-300 flex items-center gap-2">
                      <Zap className="w-4 h-4 text-amber-400" /> Sampling Rate
                    </span>
                    <span className="font-mono text-blue-300">{params.samplingRate} Hz</span>
                  </div>
                  <input 
                    type="range" 
                    min="10" max="200" step="5"
                    value={params.samplingRate}
                    onChange={(e) => {
                      setParams({...params, samplingRate: parseInt(e.target.value)});
                      setActiveScenario('custom');
                    }}
                    className="w-full h-2 bg-gray-700 rounded-lg appearance-none cursor-pointer accent-blue-500"
                  />
                  <div className="flex justify-between text-xs text-gray-500">
                    <span>10</span>
                    <span>200</span>
                  </div>
                </div>

                {/* Airflow Target */}
                <div className="space-y-3">
                  <div className="flex justify-between text-sm">
                    <span className="text-gray-300 flex items-center gap-2">
                      <Wind className="w-4 h-4 text-cyan-400" /> Airflow Target
                    </span>
                    <span className="font-mono text-blue-300">{params.airflowTarget} L/min</span>
                  </div>
                  <input 
                    type="range" 
                    min="10" max="100" step="1"
                    value={params.airflowTarget}
                    onChange={(e) => {
                      setParams({...params, airflowTarget: parseInt(e.target.value)});
                      setActiveScenario('custom');
                    }}
                    className="w-full h-2 bg-gray-700 rounded-lg appearance-none cursor-pointer accent-blue-500"
                  />
                  <div className="flex justify-between text-xs text-gray-500">
                    <span>10</span>
                    <span>100</span>
                  </div>
                </div>

                {/* Processing Power */}
                <div className="space-y-3">
                  <div className="flex justify-between text-sm">
                    <span className="text-gray-300 flex items-center gap-2">
                      <Gauge className="w-4 h-4 text-purple-400" /> Processing Power
                    </span>
                    <span className="font-mono text-blue-300">{params.processingPower}%</span>
                  </div>
                  <input 
                    type="range" 
                    min="20" max="100" step="5"
                    value={params.processingPower}
                    onChange={(e) => {
                      setParams({...params, processingPower: parseInt(e.target.value)});
                      setActiveScenario('custom');
                    }}
                    className="w-full h-2 bg-gray-700 rounded-lg appearance-none cursor-pointer accent-blue-500"
                  />
                  <div className="flex justify-between text-xs text-gray-500">
                    <span>20</span>
                    <span>100</span>
                  </div>
                </div>

                {/* Thermal Threshold */}
                <div className="space-y-3">
                  <div className="flex justify-between text-sm">
                    <span className="text-gray-300 flex items-center gap-2">
                      <Thermometer className="w-4 h-4 text-red-400" /> Thermal Limit
                    </span>
                    <span className="font-mono text-blue-300">{params.thermalThreshold}°C</span>
                  </div>
                  <input 
                    type="range" 
                    min="45" max="75" step="1"
                    value={params.thermalThreshold}
                    onChange={(e) => {
                      setParams({...params, thermalThreshold: parseInt(e.target.value)});
                      setActiveScenario('custom');
                    }}
                    className="w-full h-2 bg-gray-700 rounded-lg appearance-none cursor-pointer accent-blue-500"
                  />
                  <div className="flex justify-between text-xs text-gray-500">
                    <span>45</span>
                    <span>75</span>
                  </div>
                </div>

                {/* Power Mode */}
                <div className="space-y-3">
                  <span className="text-gray-300 text-sm flex items-center gap-2">
                    <Activity className="w-4 h-4 text-indigo-400" /> Power Mode
                  </span>
                  <div className="flex gap-2">
                    {(['eco', 'balanced', 'performance'] as const).map((mode) => (
                      <button
                        key={mode}
                        onClick={() => {
                          setParams({...params, powerMode: mode});
                          setActiveScenario('custom');
                        }}
                        className={`flex-1 py-2 px-3 rounded-lg text-xs font-medium transition-all ${
                          params.powerMode === mode
                            ? 'bg-indigo-500 text-white'
                            : 'bg-gray-700 text-gray-300 hover:bg-gray-600'
                        }`}
                      >
                        {mode.charAt(0).toUpperCase() + mode.slice(1)}
                      </button>
                    ))}
                  </div>
                </div>
              </div>

              <div className="mt-8 flex gap-3">
                <button 
                  onClick={runSimulation}
                  disabled={isSimulating}
                  className="flex-1 bg-blue-600 hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed text-white py-3 rounded-lg font-medium flex items-center justify-center gap-2 transition-all shadow-lg shadow-blue-500/20">
                  {isSimulating ? (
                    <RotateCcw className="w-4 h-4 animate-spin" />
                  ) : (
                    <Play className="w-4 h-4" />
                  )}
                  {isSimulating ? 'Simulating...' : 'Run Analysis'}
                </button>
                <button 
                  onClick={applyAIOptimization}
                  className="px-4 py-3 bg-gradient-to-r from-purple-600 to-pink-600 hover:from-purple-700 hover:to-pink-700 text-white rounded-lg font-medium flex items-center gap-2 transition-all shadow-lg shadow-purple-500/20">
                  <Sparkles className="w-4 h-4" />
                  AI Optimize
                </button>
              </div>
            </div>

            {/* Enhanced AI Insights */}
            <div className="bg-gradient-to-br from-purple-900/40 to-blue-900/40 border border-purple-500/20 p-5 rounded-xl">
              <h3 className="text-xs font-bold text-purple-300 uppercase mb-3 flex items-center gap-2">
                <Sparkles className="w-3 h-3" /> AI Insights & Recommendations
              </h3>
              <div className="space-y-3">
                {simResults.riskScore > 30 && (
                  <div className="flex items-start gap-2 p-2 bg-red-500/10 border border-red-500/20 rounded">
                    <AlertTriangle className="w-4 h-4 text-red-400 mt-0.5" />
                    <div className="text-xs text-red-300">
                      High risk detected. Consider reducing processing power or thermal threshold.
                    </div>
                  </div>
                )}
                {simResults.batteryLife < 8 && (
                  <div className="flex items-start gap-2 p-2 bg-yellow-500/10 border border-yellow-500/20 rounded">
                    <Info className="w-4 h-4 text-yellow-400 mt-0.5" />
                    <div className="text-xs text-yellow-300">
                      Battery life below 8 hours. Increase capacity or reduce power consumption.
                    </div>
                  </div>
                )}
                {simResults.complianceScore > 95 && simResults.riskScore < 20 && (
                  <div className="flex items-start gap-2 p-2 bg-green-500/10 border border-green-500/20 rounded">
                    <CheckCircle2 className="w-4 h-4 text-green-400 mt-0.5" />
                    <div className="text-xs text-green-300">
                      Excellent configuration! All parameters within optimal ranges.
                    </div>
                  </div>
                )}
                <div className="text-xs text-gray-300 leading-relaxed">
                  Current configuration provides <span className="text-white font-semibold">{simResults.batteryLife.toFixed(1)} hours</span> of operation 
                  with <span className="text-green-400 font-semibold">{simResults.efficiency.toFixed(0)}%</span> efficiency.
                </div>
              </div>
            </div>
          </div>

          {/* Right Panel: Enhanced Results */}
          <div className="xl:col-span-8 space-y-6">
            
            {/* Enhanced KPI Grid */}
            <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
              <ResultCard 
                label="Battery Life" 
                value={`${simResults.batteryLife.toFixed(1)}h`} 
                trend={simResults.batteryLife > 12 ? "up" : "down"}
                trendVal={simResults.batteryLife > 12 ? "+15%" : "-8%"}
                color="text-green-400"
                icon={<Battery className="w-4 h-4" />}
              />
              <ResultCard 
                label="Thermal Load" 
                value={`${simResults.thermalLoad.toFixed(0)}°C`} 
                trend={simResults.thermalLoad < 50 ? "down" : "up"}
                trendVal={simResults.thermalLoad < 50 ? "-12%" : "+18%"}
                color="text-amber-400"
                icon={<Thermometer className="w-4 h-4" />}
                inverse={true}
              />
              <ResultCard 
                label="Efficiency" 
                value={`${simResults.efficiency.toFixed(0)}%`} 
                trend={simResults.efficiency > 85 ? "up" : "down"}
                trendVal={simResults.efficiency > 85 ? "+7%" : "-5%"}
                color="text-blue-400"
                icon={<Gauge className="w-4 h-4" />}
              />
              <ResultCard 
                label="Reliability" 
                value={`${simResults.reliability.toFixed(0)}%`} 
                trend={simResults.reliability > 90 ? "up" : "down"}
                trendVal={simResults.reliability > 90 ? "+3%" : "-6%"}
                color="text-purple-400"
                icon={<CheckCircle2 className="w-4 h-4" />}
              />
            </div>

            {/* Chart Selection & Visualization */}
            <div className="bg-gray-900 border border-gray-800 rounded-xl p-6">
              <div className="flex items-center justify-between mb-6">
                <h3 className="text-lg font-semibold text-white">Performance Analysis</h3>
                <div className="flex gap-2">
                  {[
                    { key: 'performance', label: 'Performance', icon: BarChart3 },
                    { key: 'thermal', label: 'Thermal', icon: Thermometer },
                    { key: 'battery', label: 'Battery', icon: Battery },
                    { key: 'risk', label: 'Risk', icon: AlertTriangle }
                  ].map(({ key, label, icon: Icon }) => (
                    <button
                      key={key}
                      onClick={() => setSelectedChart(key as any)}
                      className={`flex items-center gap-2 px-3 py-2 rounded-lg text-xs font-medium transition-all ${
                        selectedChart === key
                          ? 'bg-blue-600 text-white'
                          : 'bg-gray-800 text-gray-400 hover:bg-gray-700'
                      }`}
                    >
                      <Icon className="w-3 h-3" />
                      {label}
                    </button>
                  ))}
                </div>
              </div>
              
              <div className="h-80 flex items-end gap-1 relative">
                {/* Enhanced Grid Lines */}
                <div className="absolute inset-0 border-l border-b border-gray-800 pointer-events-none">
                  {[1,2,3,4].map(i => (
                    <div key={i} className="absolute w-full border-t border-gray-800/50" style={{bottom: `${i*25}%`}}>
                      <span className="absolute -left-8 -top-2 text-xs text-gray-600">
                        {selectedChart === 'battery' ? `${100 - i*25}%` : 
                         selectedChart === 'thermal' ? `${20 + i*15}°C` :
                         `${i*25}%`}
                      </span>
                    </div>
                  ))}
                </div>

                {/* Enhanced Chart Bars */}
                {chartData.map((data, i) => {
                  const height = Math.max(5, (data.value / (selectedChart === 'thermal' ? 80 : 100)) * 100);
                  const colorClass = selectedChart === 'performance' ? 'from-blue-600/30 to-blue-500/70' :
                                   selectedChart === 'thermal' ? 'from-red-600/30 to-red-500/70' :
                                   selectedChart === 'battery' ? 'from-green-600/30 to-green-500/70' :
                                   'from-orange-600/30 to-orange-500/70';
                  
                  return (
                    <div 
                      key={i} 
                      className={`flex-1 bg-gradient-to-t ${colorClass} rounded-t-sm hover:opacity-80 transition-all cursor-pointer relative group`}
                      style={{ height: `${height}%` }}
                    >
                      <div className="absolute -top-12 left-1/2 -translate-x-1/2 bg-gray-800 text-xs px-2 py-1 rounded border border-gray-700 opacity-0 group-hover:opacity-100 transition-opacity whitespace-nowrap z-10">
                        <div className="font-medium">{data.label}</div>
                        <div className="text-gray-300">Hour {data.hour}: {data.value.toFixed(1)}</div>
                      </div>
                    </div>
                  );
                })}
              </div>
              
              <div className="flex justify-between mt-4 text-xs text-gray-500 font-mono">
                <span>0h</span>
                <span>6h</span>
                <span>12h</span>
                <span>18h</span>
                <span>24h</span>
              </div>
            </div>

            {/* Additional Metrics */}
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
              <div className="bg-gray-800/50 border border-gray-700 rounded-xl p-6">
                <h4 className="text-lg font-semibold mb-4 flex items-center gap-2">
                  <PieChart className="w-5 h-5 text-green-400" />
                  Cost Impact Analysis
                </h4>
                <div className="space-y-3">
                  <div className="flex justify-between items-center">
                    <span className="text-gray-400">Battery Cost</span>
                    <span className={`font-mono ${simResults.costImpact > 0 ? 'text-red-400' : 'text-green-400'}`}>
                      {simResults.costImpact > 0 ? '+' : ''}${Math.abs(simResults.costImpact).toFixed(0)}
                    </span>
                  </div>
                  <div className="flex justify-between items-center">
                    <span className="text-gray-400">Processing Cost</span>
                    <span className="font-mono text-gray-300">+$12</span>
                  </div>
                  <div className="flex justify-between items-center">
                    <span className="text-gray-400">Efficiency Savings</span>
                    <span className="font-mono text-green-400">-$8</span>
                  </div>
                  <div className="border-t border-gray-700 pt-2 flex justify-between items-center font-semibold">
                    <span className="text-white">Total Impact</span>
                    <span className={`font-mono ${simResults.costImpact > 0 ? 'text-red-400' : 'text-green-400'}`}>
                      {simResults.costImpact > 0 ? '+' : ''}${simResults.costImpact.toFixed(0)}
                    </span>
                  </div>
                </div>
              </div>

              <div className="bg-gray-800/50 border border-gray-700 rounded-xl p-6">
                <h4 className="text-lg font-semibold mb-4 flex items-center gap-2">
                  <LineChart className="w-5 h-5 text-purple-400" />
                  Simulation History
                </h4>
                <div className="space-y-2">
                  {simulationHistory.slice(-5).map((result, i) => (
                    <div key={i} className="flex justify-between items-center py-2 border-b border-gray-700/50 last:border-b-0">
                      <span className="text-gray-400 text-sm">Run #{simulationHistory.length - 4 + i}</span>
                      <div className="flex gap-3 text-xs">
                        <span className="text-green-400">{result.batteryLife.toFixed(1)}h</span>
                        <span className="text-amber-400">{result.thermalLoad.toFixed(0)}°C</span>
                        <span className="text-blue-400">{result.efficiency.toFixed(0)}%</span>
                      </div>
                    </div>
                  ))}
                  {simulationHistory.length === 0 && (
                    <div className="text-gray-500 text-sm text-center py-4">
                      No simulation history yet
                    </div>
                  )}
                </div>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}

function ResultCard({ label, value, trend, trendVal, color, icon, inverse = false }: {
  label: string;
  value: string;
  trend: 'up' | 'down';
  trendVal: string;
  color: string;
  icon: React.ReactNode;
  inverse?: boolean;
}) {
  const isPositive = trend === 'up';
  let trendColor = 'text-green-400';
  let Icon = TrendingUp;

  if (inverse) {
    trendColor = isPositive ? 'text-red-400' : 'text-green-400';
    Icon = isPositive ? TrendingUp : TrendingDown;
  } else {
    trendColor = isPositive ? 'text-green-400' : 'text-red-400';
    Icon = isPositive ? TrendingUp : TrendingDown;
  }

  return (
    <div className="bg-gray-800/50 border border-gray-700 p-4 rounded-xl hover:bg-gray-800/70 transition-all">
      <div className="flex items-center justify-between mb-3">
        <div className={`p-2 rounded-lg bg-gray-700/50 ${color}`}>
          {icon}
        </div>
        <div className={`flex items-center gap-1 text-xs font-medium ${trendColor} bg-gray-800/50 px-2 py-1 rounded`}>
          <Icon className="w-3 h-3" />
          {trendVal}
        </div>
      </div>
      <div className="text-gray-400 text-xs uppercase tracking-wider mb-1">{label}</div>
      <div className={`text-xl font-bold font-mono ${color}`}>{value}</div>
    </div>
  );
}