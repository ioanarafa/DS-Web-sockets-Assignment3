import React, { useState, useEffect } from 'react';
import { Zap, Users, Cpu, LogOut, Plus, Edit2, Trash2, Link } from 'lucide-react';

const API_URL = 'http://localhost';

const EnergyManagementSystem = () => {
    const [token, setToken] = useState(localStorage.getItem('token'));
    const [userRole, setUserRole] = useState(localStorage.getItem('role'));
    const [username, setUsername] = useState(localStorage.getItem('username'));
    const [currentView, setCurrentView] = useState('login');

    const [loginForm, setLoginForm] = useState({ username: '', password: '' });
    const [users, setUsers] = useState([]);
    const [devices, setDevices] = useState([]);
    const [editingUser, setEditingUser] = useState(null);
    const [editingDevice, setEditingDevice] = useState(null);
    const [assignModal, setAssignModal] = useState(null);

    useEffect(() => {
        if (token && userRole) {
            setCurrentView(userRole === 'ADMIN' ? 'users' : 'client-devices');
            if (userRole === 'ADMIN') {
                fetchUsers();
                fetchDevices();
            } else {
                fetchClientDevices();
            }
        }
    }, [token, userRole]);

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch(`${API_URL}/auth/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(loginForm)
            });

            if (response.ok) {
                const jwtToken = await response.text();
                const payload = JSON.parse(atob(jwtToken.split('.')[1]));
                const role = payload.role || 'CLIENT';

                localStorage.setItem('token', jwtToken);
                localStorage.setItem('role', role);
                localStorage.setItem('username', loginForm.username);

                setToken(jwtToken);
                setUserRole(role);
                setUsername(loginForm.username);
                setCurrentView(role === 'ADMIN' ? 'users' : 'client-devices');
            } else {
                alert('Login failed!');
            }
        } catch (error) {
            alert('Error: ' + error.message);
        }
    };

    const handleLogout = () => {
        localStorage.clear();
        setToken(null);
        setUserRole(null);
        setUsername(null);
        setCurrentView('login');
    };

    const fetchUsers = async () => {
        try {
            const response = await fetch(`${API_URL}/users`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (response.ok) {
                const data = await response.json();
                setUsers(data);
            }
        } catch (error) {
            console.error('Error fetching users:', error);
        }
    };

    const fetchDevices = async () => {
        try {
            const response = await fetch(`${API_URL}/devices`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (response.ok) {
                const data = await response.json();
                setDevices(data);
            }
        } catch (error) {
            console.error('Error fetching devices:', error);
        }
    };

    const fetchClientDevices = async () => {
        try {

            const jwtToken = localStorage.getItem('token');
            const payload = JSON.parse(atob(jwtToken.split('.')[1]));
            const userId = payload.userId;

            const response = await fetch(`${API_URL}/devices/user/${userId}`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (response.ok) {
                const data = await response.json();
                setDevices(data);
            }
        } catch (error) {
            console.error('Error fetching client devices:', error);
        }
    };

    const createUser = async (userData) => {
        try {
            const response = await fetch(`${API_URL}/users`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(userData)
            });
            if (response.ok) {
                fetchUsers();
                setEditingUser(null);
            }
        } catch (error) {
            alert('Error creating user: ' + error.message);
        }
    };

    const updateUser = async (id, userData) => {
        try {
            const response = await fetch(`${API_URL}/users/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(userData)
            });
            if (response.ok) {
                fetchUsers();
                setEditingUser(null);
            }
        } catch (error) {
            alert('Error updating user: ' + error.message);
        }
    };

    const deleteUser = async (id) => {
        if (!window.confirm('Are you sure you want to delete this user?')) return;
        try {
            const response = await fetch(`${API_URL}/users/${id}`, {
                method: 'DELETE',
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (response.ok) fetchUsers();
        } catch (error) {
            alert('Error deleting user: ' + error.message);
        }
    };

    const createDevice = async (deviceData) => {
        try {
            const response = await fetch(`${API_URL}/devices`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(deviceData)
            });
            if (response.ok) {
                fetchDevices();
                setEditingDevice(null);
            }
        } catch (error) {
            alert('Error creating device: ' + error.message);
        }
    };

    const updateDevice = async (id, deviceData) => {
        try {
            const response = await fetch(`${API_URL}/devices/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(deviceData)
            });
            if (response.ok) {
                fetchDevices();
                setEditingDevice(null);
            }
        } catch (error) {
            alert('Error updating device: ' + error.message);
        }
    };

    const deleteDevice = async (id) => {
        if (!window.confirm('Are you sure you want to delete this device?')) return;
        try {
            const response = await fetch(`${API_URL}/devices/${id}`, {
                method: 'DELETE',
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (response.ok) fetchDevices();
        } catch (error) {
            alert('Error deleting device: ' + error.message);
        }
    };

    const assignDevice = async (deviceId, userId) => {
        try {
            const response = await fetch(`${API_URL}/devices/${deviceId}/assign/${userId}`, {
                method: 'POST',
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (response.ok) {
                fetchDevices();
                setAssignModal(null);
            }
        } catch (error) {
            alert('Error assigning device: ' + error.message);
        }
    };

    if (!token) {
        return (
            <div className="min-h-screen bg-gradient-to-br from-slate-900 via-blue-900 to-slate-900 flex items-center justify-center p-4">
                <div className="bg-slate-800/50 backdrop-blur-xl rounded-2xl shadow-2xl p-8 w-full max-w-md border border-blue-500/20">
                    <div className="flex items-center justify-center mb-8">
                        <div className="bg-blue-500/20 p-4 rounded-full">
                            <Zap className="w-12 h-12 text-blue-400" />
                        </div>
                    </div>
                    <h1 className="text-3xl font-bold text-center text-white mb-2">Energy Management</h1>
                    <p className="text-center text-slate-400 mb-8">Smart Metering System</p>

                    <div className="space-y-4">
                        <div>
                            <label className="block text-sm font-medium text-slate-300 mb-2">Username</label>
                            <input
                                type="text"
                                value={loginForm.username}
                                onChange={(e) => setLoginForm({...loginForm, username: e.target.value})}
                                className="w-full px-4 py-3 bg-slate-700/50 border border-slate-600 rounded-lg text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500"
                                placeholder="Enter username"
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-slate-300 mb-2">Password</label>
                            <input
                                type="password"
                                value={loginForm.password}
                                onChange={(e) => setLoginForm({...loginForm, password: e.target.value})}
                                className="w-full px-4 py-3 bg-slate-700/50 border border-slate-600 rounded-lg text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500"
                                placeholder="Enter password"
                            />
                        </div>
                        <button
                            onClick={handleLogin}
                            className="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold py-3 rounded-lg transition-colors shadow-lg shadow-blue-500/30"
                        >
                            Sign In
                        </button>
                    </div>
                </div>
            </div>
        );
    }

    if (userRole === 'ADMIN') {
        return (
            <div className="min-h-screen bg-gradient-to-br from-slate-900 via-blue-900 to-slate-900">
                <div className="bg-slate-800/50 backdrop-blur-xl border-b border-blue-500/20">
                    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                        <div className="flex items-center justify-between h-16">
                            <div className="flex items-center space-x-3">
                                <Zap className="w-8 h-8 text-blue-400" />
                                <span className="text-xl font-bold text-white">Energy Admin</span>
                            </div>
                            <div className="flex items-center space-x-4">
                                <span className="text-slate-300">Welcome, {username}</span>
                                <button
                                    onClick={handleLogout}
                                    className="flex items-center space-x-2 px-4 py-2 bg-red-600/20 hover:bg-red-600/30 text-red-400 rounded-lg transition-colors"
                                >
                                    <LogOut className="w-4 h-4" />
                                    <span>Logout</span>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 mt-8">
                    <div className="flex space-x-4 mb-8">
                        <button
                            onClick={() => setCurrentView('users')}
                            className={`flex items-center space-x-2 px-6 py-3 rounded-lg font-semibold transition-colors ${
                                currentView === 'users'
                                    ? 'bg-blue-600 text-white shadow-lg shadow-blue-500/30'
                                    : 'bg-slate-800/50 text-slate-300 hover:bg-slate-700/50'
                            }`}
                        >
                            <Users className="w-5 h-5" />
                            <span>Users</span>
                        </button>
                        <button
                            onClick={() => setCurrentView('devices')}
                            className={`flex items-center space-x-2 px-6 py-3 rounded-lg font-semibold transition-colors ${
                                currentView === 'devices'
                                    ? 'bg-blue-600 text-white shadow-lg shadow-blue-500/30'
                                    : 'bg-slate-800/50 text-slate-300 hover:bg-slate-700/50'
                            }`}
                        >
                            <Cpu className="w-5 h-5" />
                            <span>Devices</span>
                        </button>
                    </div>

                    {currentView === 'users' && (
                        <div>
                            <div className="flex justify-between items-center mb-6">
                                <h2 className="text-2xl font-bold text-white">User Management</h2>
                                <button
                                    onClick={() => setEditingUser({ username: '', password: '', email: '', fullName: '' })}
                                    className="flex items-center space-x-2 px-4 py-2 bg-green-600 hover:bg-green-700 text-white rounded-lg transition-colors"
                                >
                                    <Plus className="w-4 h-4" />
                                    <span>Add User</span>
                                </button>
                            </div>

                            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                                {users.map(user => (
                                    <div key={user.id} className="bg-slate-800/50 backdrop-blur-xl rounded-lg p-6 border border-blue-500/20 hover:border-blue-500/40 transition-colors">
                                        <div className="flex items-start justify-between mb-4">
                                            <div className="flex-1">
                                                <h3 className="text-lg font-semibold text-white">{user.username}</h3>
                                                <p className="text-slate-400 text-sm">{user.email}</p>
                                                <p className="text-slate-500 text-xs mt-1">{user.fullName}</p>
                                            </div>
                                        </div>
                                        <div className="flex space-x-2">
                                            <button
                                                onClick={() => setEditingUser(user)}
                                                className="flex-1 flex items-center justify-center space-x-1 px-3 py-2 bg-blue-600/20 hover:bg-blue-600/30 text-blue-400 rounded-lg transition-colors"
                                            >
                                                <Edit2 className="w-4 h-4" />
                                                <span>Edit</span>
                                            </button>
                                            <button
                                                onClick={() => deleteUser(user.id)}
                                                className="flex-1 flex items-center justify-center space-x-1 px-3 py-2 bg-red-600/20 hover:bg-red-600/30 text-red-400 rounded-lg transition-colors"
                                            >
                                                <Trash2 className="w-4 h-4" />
                                                <span>Delete</span>
                                            </button>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    )}

                    {currentView === 'devices' && (
                        <div>
                            <div className="flex justify-between items-center mb-6">
                                <h2 className="text-2xl font-bold text-white">Device Management</h2>
                                <button
                                    onClick={() => setEditingDevice({ name: '', description: '', address: '', maxConsumption: '' })}
                                    className="flex items-center space-x-2 px-4 py-2 bg-green-600 hover:bg-green-700 text-white rounded-lg transition-colors"
                                >
                                    <Plus className="w-4 h-4" />
                                    <span>Add Device</span>
                                </button>
                            </div>

                            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                                {devices.map(device => (
                                    <div key={device.id} className="bg-slate-800/50 backdrop-blur-xl rounded-lg p-6 border border-blue-500/20 hover:border-blue-500/40 transition-colors">
                                        <div className="flex items-start justify-between mb-4">
                                            <div className="flex-1">
                                                <h3 className="text-lg font-semibold text-white">{device.name}</h3>
                                                <p className="text-slate-400 text-sm">{device.description}</p>
                                                <p className="text-slate-500 text-xs mt-1">{device.address}</p>
                                                <p className="text-blue-400 text-sm mt-2 font-semibold">Max: {device.maxConsumption} kW</p>
                                            </div>
                                        </div>
                                        <div className="flex space-x-2">
                                            <button
                                                onClick={() => setAssignModal(device)}
                                                className="flex-1 flex items-center justify-center space-x-1 px-3 py-2 bg-purple-600/20 hover:bg-purple-600/30 text-purple-400 rounded-lg transition-colors"
                                            >
                                                <Link className="w-4 h-4" />
                                                <span>Assign</span>
                                            </button>
                                            <button
                                                onClick={() => setEditingDevice(device)}
                                                className="flex-1 flex items-center justify-center space-x-1 px-3 py-2 bg-blue-600/20 hover:bg-blue-600/30 text-blue-400 rounded-lg transition-colors"
                                            >
                                                <Edit2 className="w-4 h-4" />
                                                <span>Edit</span>
                                            </button>
                                            <button
                                                onClick={() => deleteDevice(device.id)}
                                                className="flex-1 flex items-center justify-center space-x-1 px-3 py-2 bg-red-600/20 hover:bg-red-600/30 text-red-400 rounded-lg transition-colors"
                                            >
                                                <Trash2 className="w-4 h-4" />
                                            </button>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    )}
                </div>

                {editingUser && (
                    <div className="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center p-4 z-50">
                        <div className="bg-slate-800 rounded-xl p-6 w-full max-w-md border border-blue-500/20">
                            <h3 className="text-xl font-bold text-white mb-4">
                                {editingUser.id ? 'Edit User' : 'Create User'}
                            </h3>
                            <div className="space-y-4">
                                <input
                                    type="text"
                                    placeholder="Username"
                                    value={editingUser.username}
                                    onChange={(e) => setEditingUser({...editingUser, username: e.target.value})}
                                    className="w-full px-4 py-2 bg-slate-700 border border-slate-600 rounded-lg text-white"
                                />
                                <input
                                    type="password"
                                    placeholder="Password"
                                    value={editingUser.password}
                                    onChange={(e) => setEditingUser({...editingUser, password: e.target.value})}
                                    className="w-full px-4 py-2 bg-slate-700 border border-slate-600 rounded-lg text-white"
                                />
                                <input
                                    type="email"
                                    placeholder="Email"
                                    value={editingUser.email}
                                    onChange={(e) => setEditingUser({...editingUser, email: e.target.value})}
                                    className="w-full px-4 py-2 bg-slate-700 border border-slate-600 rounded-lg text-white"
                                />
                                <input
                                    type="text"
                                    placeholder="Full Name"
                                    value={editingUser.fullName}
                                    onChange={(e) => setEditingUser({...editingUser, fullName: e.target.value})}
                                    className="w-full px-4 py-2 bg-slate-700 border border-slate-600 rounded-lg text-white"
                                />
                                <div className="flex space-x-3">
                                    <button
                                        onClick={() => editingUser.id ? updateUser(editingUser.id, editingUser) : createUser(editingUser)}
                                        className="flex-1 bg-blue-600 hover:bg-blue-700 text-white py-2 rounded-lg transition-colors"
                                    >
                                        {editingUser.id ? 'Update' : 'Create'}
                                    </button>
                                    <button
                                        onClick={() => setEditingUser(null)}
                                        className="flex-1 bg-slate-700 hover:bg-slate-600 text-white py-2 rounded-lg transition-colors"
                                    >
                                        Cancel
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                )}

                {editingDevice && (
                    <div className="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center p-4 z-50">
                        <div className="bg-slate-800 rounded-xl p-6 w-full max-w-md border border-blue-500/20">
                            <h3 className="text-xl font-bold text-white mb-4">
                                {editingDevice.id ? 'Edit Device' : 'Create Device'}
                            </h3>
                            <div className="space-y-4">
                                <input
                                    type="text"
                                    placeholder="Device Name"
                                    value={editingDevice.name}
                                    onChange={(e) => setEditingDevice({...editingDevice, name: e.target.value})}
                                    className="w-full px-4 py-2 bg-slate-700 border border-slate-600 rounded-lg text-white"
                                />
                                <input
                                    type="text"
                                    placeholder="Description"
                                    value={editingDevice.description}
                                    onChange={(e) => setEditingDevice({...editingDevice, description: e.target.value})}
                                    className="w-full px-4 py-2 bg-slate-700 border border-slate-600 rounded-lg text-white"
                                />
                                <input
                                    type="text"
                                    placeholder="Address"
                                    value={editingDevice.address}
                                    onChange={(e) => setEditingDevice({...editingDevice, address: e.target.value})}
                                    className="w-full px-4 py-2 bg-slate-700 border border-slate-600 rounded-lg text-white"
                                />
                                <input
                                    type="number"
                                    placeholder="Max Consumption (kW)"
                                    value={editingDevice.maxConsumption}
                                    onChange={(e) => setEditingDevice({...editingDevice, maxConsumption: e.target.value})}
                                    className="w-full px-4 py-2 bg-slate-700 border border-slate-600 rounded-lg text-white"
                                />
                                <div className="flex space-x-3">
                                    <button
                                        onClick={() => editingDevice.id ? updateDevice(editingDevice.id, editingDevice) : createDevice(editingDevice)}
                                        className="flex-1 bg-blue-600 hover:bg-blue-700 text-white py-2 rounded-lg transition-colors"
                                    >
                                        {editingDevice.id ? 'Update' : 'Create'}
                                    </button>
                                    <button
                                        onClick={() => setEditingDevice(null)}
                                        className="flex-1 bg-slate-700 hover:bg-slate-600 text-white py-2 rounded-lg transition-colors"
                                    >
                                        Cancel
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                )}

                {assignModal && (
                    <div className="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center p-4 z-50">
                        <div className="bg-slate-800 rounded-xl p-6 w-full max-w-md border border-blue-500/20">
                            <h3 className="text-xl font-bold text-white mb-4">Assign Device: {assignModal.name}</h3>
                            <div className="space-y-2 max-h-96 overflow-y-auto">
                                {users.map(user => (
                                    <button
                                        key={user.id}
                                        onClick={() => assignDevice(assignModal.id, user.id)}
                                        className="w-full text-left px-4 py-3 bg-slate-700 hover:bg-slate-600 text-white rounded-lg transition-colors"
                                    >
                                        <div className="font-semibold">{user.username}</div>
                                        <div className="text-sm text-slate-400">{user.email}</div>
                                    </button>
                                ))}
                            </div>
                            <button
                                onClick={() => setAssignModal(null)}
                                className="w-full mt-4 bg-slate-700 hover:bg-slate-600 text-white py-2 rounded-lg transition-colors"
                            >
                                Cancel
                            </button>
                        </div>
                    </div>
                )}
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gradient-to-br from-slate-900 via-blue-900 to-slate-900">
            <div className="bg-slate-800/50 backdrop-blur-xl border-b border-blue-500/20">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="flex items-center justify-between h-16">
                        <div className="flex items-center space-x-3">
                            <Zap className="w-8 h-8 text-blue-400" />
                            <span className="text-xl font-bold text-white">My Devices</span>
                        </div>
                        <div className="flex items-center space-x-4">
                            <span className="text-slate-300">Welcome, {username}</span>
                            <button
                                onClick={handleLogout}
                                className="flex items-center space-x-2 px-4 py-2 bg-red-600/20 hover:bg-red-600/30 text-red-400 rounded-lg transition-colors"
                            >
                                <LogOut className="w-4 h-4" />
                                <span>Logout</span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 mt-8">
                <h2 className="text-2xl font-bold text-white mb-6">Your Energy Devices</h2>

                {devices.length === 0 ? (
                    <div className="bg-slate-800/50 backdrop-blur-xl rounded-xl p-12 text-center border border-blue-500/20">
                        <Cpu className="w-16 h-16 text-slate-600 mx-auto mb-4" />
                        <p className="text-slate-400 text-lg">No devices assigned yet</p>
                    </div>
                ) : (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {devices.map(device => (
                            <div key={device.id} className="bg-slate-800/50 backdrop-blur-xl rounded-xl p-6 border border-blue-500/20 hover:border-blue-500/40 transition-all hover:shadow-lg hover:shadow-blue-500/20">
                                <div className="flex items-center space-x-4 mb-4">
                                    <div className="bg-blue-500/20 p-3 rounded-lg">
                                        <Cpu className="w-8 h-8 text-blue-400" />
                                    </div>
                                    <div className="flex-1">
                                        <h3 className="text-lg font-semibold text-white">{device.name}</h3>
                                        <p className="text-slate-400 text-sm">{device.description}</p>
                                    </div>
                                </div>
                                <div className="space-y-2">
                                    <div className="flex justify-between items-center">
                                        <span className="text-slate-400 text-sm">Location</span>
                                        <span className="text-white text-sm">{device.address}</span>
                                    </div>
                                    <div className="flex justify-between items-center">
                                        <span className="text-slate-400 text-sm">Max Consumption</span>
                                        <span className="text-blue-400 font-semibold">{device.maxConsumption} kW</span>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};

export default EnergyManagementSystem;