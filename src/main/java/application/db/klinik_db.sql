-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 03, 2025 at 04:58 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `klinik_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `dokter`
--

CREATE TABLE `dokter` (
  `id_dokter` varchar(20) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `spesialisasi` varchar(100) NOT NULL,
  `telepon` varchar(20) NOT NULL,
  `jam_mulai_praktik` time NOT NULL,
  `jam_selesai_praktik` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `dokter`
--

INSERT INTO `dokter` (`id_dokter`, `nama`, `spesialisasi`, `telepon`, `jam_mulai_praktik`, `jam_selesai_praktik`) VALUES
('D001', 'Harits', 'Gigi', '089999999999', '13:00:00', '15:00:00'),
('D002', 'Farhan', 'Mata', '082222222222', '10:00:00', '15:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `obat`
--

CREATE TABLE `obat` (
  `kode_obat` varchar(20) NOT NULL,
  `nama_obat` varchar(100) NOT NULL,
  `produsen` varchar(100) DEFAULT NULL,
  `satuan` varchar(50) DEFAULT NULL,
  `stok` int(11) NOT NULL DEFAULT 0,
  `harga_beli` decimal(10,2) DEFAULT NULL,
  `harga_jual` decimal(10,2) DEFAULT NULL,
  `tanggal_kadaluarsa` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `obat`
--

INSERT INTO `obat` (`kode_obat`, `nama_obat`, `produsen`, `satuan`, `stok`, `harga_beli`, `harga_jual`, `tanggal_kadaluarsa`) VALUES
('OBT001', 'Paracetamol 500mg', 'PT Pharma Sehat', 'Tablet', 150, 1500.00, 2000.00, '2026-12-31'),
('OBT002', 'Amoxicillin 250mg', 'PT Medika Jaya', 'Kapsul', 75, 3500.00, 4500.00, '2025-08-15'),
('OBT003', 'Vitamin C Tablet Hisap', 'PT Nutri Farma', 'Tablet', 200, 800.00, 1200.00, '2027-01-20'),
('OBT004', 'Obat Batuk Sirup Anak', 'PT Herbal Indo', 'Botol 60ml', 50, 12000.00, 15000.00, '2025-11-30'),
('OBT005', 'Antasida Doen Suspensi', 'PT Generik Nasional', 'Botol 100ml', 90, 6000.00, 7500.00, '2026-06-01');

-- --------------------------------------------------------

--
-- Table structure for table `pasien`
--

CREATE TABLE `pasien` (
  `id_pasien` varchar(20) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `tanggal_lahir` date NOT NULL,
  `alamat` varchar(255) NOT NULL,
  `telepon` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pasien`
--

INSERT INTO `pasien` (`id_pasien`, `nama`, `tanggal_lahir`, `alamat`, `telepon`) VALUES
('P001', 'Rasyid', '1945-08-17', 'Jl. Babarsari No.2', '081111111111'),
('P002', 'Levi', '1950-06-30', 'Jl. In Aja Dulu', '088888888888');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`username`, `password`) VALUES
('admin', '$2a$10$bknX8WN.86wiuumfg5xN7u2jXhphJeRa2sfIa/0LFSSN48mxUdGEu');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `dokter`
--
ALTER TABLE `dokter`
  ADD PRIMARY KEY (`id_dokter`);

--
-- Indexes for table `obat`
--
ALTER TABLE `obat`
  ADD PRIMARY KEY (`kode_obat`);

--
-- Indexes for table `pasien`
--
ALTER TABLE `pasien`
  ADD PRIMARY KEY (`id_pasien`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD UNIQUE KEY `username` (`username`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
