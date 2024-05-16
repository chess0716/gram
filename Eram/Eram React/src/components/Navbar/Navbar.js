import React, { useState } from "react";
import images from "../../constants/images";
import styles from "./Navbar.module.css"; // 모듈 CSS import
import { FaBars, FaTimes } from 'react-icons/fa';

const Navbar = () => {
    const [toggleMenu, setToggleMenu] = useState(false);

    return (
        <nav className={styles.navbar}>
            <div className="container flex">
                <div className={`${styles.brand__and__toggler} flex`}>
                    <a href="index.html" alt=""
                    className={styles.navbar__brand}>
                        <img src={images.logo} alt="sitelogo" style={{ width: '110px', height: '100px' }} />
                    </a>
                    <button type="button"
                    className={`${styles.navbar__open__btn} text__light`} onClick={() => setToggleMenu(true)}>
                        <FaBars size={26}/>
                    </button>
                </div>
                <div className={styles.navbar__collapse}>
                    <ul className={styles.navbar__nav}>
                        <li className={styles.nav__item}>
                            <a href="#home" className={`${styles.nav__link} text__upper fw__6 text__light nav__active`}>Home</a>
                        </li>
                        <li className={styles.nav_item}>
                            <a href="#about" className={`${styles.nav__link} text__upper fw__6 text__light`}>ABOUT</a>
                        </li>
                        <li className={styles.nav_item}>
                            <a href="#signUp" className={`${styles.nav__link} text__upper fw__6 text__light`}>SIGNUP</a>
                        </li>
                        <li className={styles.nav_item}>
                            <a href="#signIn" className={`${styles.nav__link} text__upper fw__6 text__light`}>SIGNIN</a>
                        </li>
                        <li className={styles.nav_item}>
                            <a href="#myPage" className={`${styles.nav__link} text__upper fw__6 text__light`}>MYPAGE</a>
                        </li>
                        <li className={styles.nav_item}>
                            <a href="#place" className={`${styles.nav__link} text__upper fw__6 text__light`}>PLACE</a>
                        </li>
                    </ul>
                </div>

                {toggleMenu && (
                    <div className={styles.navbar__smallscreen}>
                        <button type="button"
                        className={`${styles.navbar__close__btn} text__light`}
                        onClick={() => setToggleMenu(false)}>
                            <FaTimes size={32} />
                        </button>
                        <ul className={`${styles.navbar__nav__smallscreen} text__light`}>
                            <li className={styles.nav__item}>
                                <a href="#home"
                                className={`${styles.nav__link} text__upper fw__6 nav__active text__light`}>
                                    Home
                                </a>
                            </li>
                            <li className={styles.nav__item}>
                                <a href="#about"
                                className={`${styles.nav__link} text__upper fw__6 text__light`}>
                                    ABOUT
                                </a>
                            </li>
                            <li className={styles.nav__item}>
                                <a href="#signUp"
                                className={`${styles.nav__link} text__upper fw__6 text__light`}>
                                    SIGNUP
                                </a>
                            </li>
                            <li className={styles.nav__item}>
                                <a href="#signIn"
                                className={`${styles.nav__link} text__upper fw__6 text__light`}>
                                    SIGNIN
                                </a>
                            </li>
                            <li className={styles.nav__item}>
                                <a href="#myPage"
                                className={`${styles.nav__link} text__upper fw__6 text__light`}>
                                    MYPAGE
                                </a>
                            </li>
                            <li className={styles.nav__item}>
                                <a href="#place"
                                className={`${styles.nav__link} text__upper fw__6 text__light`}>
                                    PLACE
                                </a>
                            </li>
                        </ul>
                    </div>
                )}
            </div>
        </nav>
    )
}

export default Navbar;
