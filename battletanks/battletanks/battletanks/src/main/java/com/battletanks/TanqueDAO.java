package com.battletanks;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TanqueDAO {
    public void inserirTanque(Tanque tanque) {
        
        String sql = "INSERT INTO tanques (id, codinome, classes, blindagem, velocidade, poderDeFogo, piloto, horaEntradaArena, integridade, status)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = Conexao.getConexao();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, tanque.getId());
            ps.setString(2, tanque.getCodinome());
            ps.setString(3, tanque.getClasse());
            ps.setInt(4, tanque.getBlindagem());
            ps.setInt(5, tanque.getVelocidade());
            ps.setInt(6, tanque.getPoderDeFogo());
            ps.setString(7, tanque.getPiloto());
            ps.setTimestamp(8, Timestamp.valueOf(tanque.getHoraEntradaArena()));
            ps.setInt(9, tanque.getIntegridade());
            ps.setString(10, tanque.getStatus());

            ps.executeUpdate();
            System.out.println("Tanque inserido com sucesso!");

        } catch (Exception e) {
            System.err.println("Erro ao inserir tanque: " + e.getMessage());
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Tanque> listar() {
        ArrayList<Tanque> lista = new ArrayList<>();
        String sql = "SELECT * FROM tanques";

        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            conn = Conexao.getConexao();
            st = conn.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                Tanque tanque = new TanquePadrao();
                tanque.setId(rs.getInt("id"));
                tanque.setCodinome(rs.getString("codinome"));
                tanque.setClasse(rs.getString("classes"));
                tanque.setBlindagem(rs.getInt("blindagem"));
                tanque.setVelocidade(rs.getInt("velocidade"));
                tanque.setPoderDeFogo(rs.getInt("poderDeFogo"));
                tanque.setPiloto(rs.getString("piloto"));

                Timestamp ts = rs.getTimestamp("horaEntradaArena");
                if (ts != null) {
                    tanque.setHoraEntradaArena(ts.toLocalDateTime());
                }
                else {
                    tanque.setHoraEntradaArena(LocalDateTime.now());
                }
                tanque.setIntegridade(rs.getInt("integridade"));
                tanque.setStatus(rs.getString("status"));

                lista.add(tanque);
            }


        } catch (Exception e) {
            System.out.println("Erro ao listar tanques: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lista;
    }
    public int contarTanques() {
        String sql = "SELECT COUNT(*) FROM tanques";

        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = Conexao.getConexao();
            st = conn.createStatement();
            rs = st.executeQuery(sql);

            if(rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public Tanque buscarPorId(int id) {
        String sql = "SELECT * FROM tanques WHERE id = ?";
        Tanque tanque = null;

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = Conexao.getConexao();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                tanque = new TanquePadrao(
                rs.getInt("id"),
                rs.getString("codinome"),
                rs.getString("classes"),
                rs.getInt("blindagem"),
                rs.getInt("velocidade"),
                rs.getInt("poderDeFogo"),
                rs.getString("piloto"),
                rs.getInt("integridade"),
                rs.getString("status"),
                rs.getTimestamp("horaEntradaArena") != null 
                    ? rs.getTimestamp("horaEntradaArena").toLocalDateTime()
                    : LocalDateTime.now()
                );
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar tanque por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return tanque;
    }


    public void excluirTanque(int id) {
        String sql = "DELETE FROM tanques WHERE ID = ?";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = Conexao.getConexao();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Tanque excluído com sucesso do banco de dados!");
            } else {
                System.out.println("Nenhum tanque encontrado com o ID " + id);
            }

        } catch (Exception e) {
           System.err.println("Erro ao excluir tanque: " + e.getMessage());
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            }
        }
    }
}
