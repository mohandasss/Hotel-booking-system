import React from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import ApiService from '../../service/ApiService';

function Navbar() {
    const isAuthenticated = ApiService.isAuthenticated();
    const isAdmin = ApiService.isAdmin();
    const isUser = ApiService.isUser();
    const navigate = useNavigate();

    const handleLogout = () => {
        const isLogout = window.confirm('Are you sure you want to logout this user?');
        if (isLogout) {
            ApiService.logout();
            navigate('/home');
        }
    };

    return (
        <nav className="navbar">
            <div className="navbar-brand">
                <img src="/assets/images/logo.png" alt="Logo" className="navbar-logo" />
                <NavLink to="/home">Mohan Hotels</NavLink>
            </div>
            <ul className="navbar-ul">
                <li><NavLink to="/home" activeClassName="active">Home</NavLink></li>
                <li><NavLink to="/rooms" activeClassName="active">Rooms</NavLink></li>
                <li><NavLink to="/find-booking" activeClassName="active">Find my Booking</NavLink></li>

                {isUser && <li><NavLink to="/profile" activeClassName="active">Profile</NavLink></li>}
                {isAdmin && <li><NavLink to="/admin" activeClassName="active">Admin</NavLink></li>}

                {!isAuthenticated && <li><NavLink to="/login" activeClassName="active">Login</NavLink></li>}
                {!isAuthenticated && <li><NavLink to="/register" activeClassName="active">Register</NavLink></li>}
                {isAuthenticated && <li onClick={handleLogout}>Logout</li>}
            </ul>
        </nav>
    );
}

export default Navbar;
