package com.alanfolha.helpdesk.api.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alanfolha.helpdesk.api.entity.ChangeStatus;
import com.alanfolha.helpdesk.api.entity.Ticket;
import com.alanfolha.helpdesk.api.repository.ChangeStatusRepository;
import com.alanfolha.helpdesk.api.repository.TicketRepository;
import com.alanfolha.helpdesk.api.service.TicketService;

@Service
public class TicketServiceImpl implements TicketService{
	
	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private ChangeStatusRepository changeStatusRepository;

	@Override
	public Ticket createOrUpdate(Ticket ticket) {
		return this.ticketRepository.save(ticket);
	}

	@Override
	public Optional<Ticket> findById(String id) {
		return this.ticketRepository.findById(id);
	}

	@Override
	public void delete(String id) {
		this.ticketRepository.deleteById(id);
	}

	@Override
	public Page<Ticket> listTicket(int page, int count) {
		Pageable pages = new PageRequest(page, count);
		return this.ticketRepository.findAll(pages);
	}

	@Override
	public ChangeStatus createChangeStatus(ChangeStatus changeStatus) {
		return this.changeStatusRepository.save(changeStatus);
	}

	@Override
	public Iterable<ChangeStatus> listChangeStatus(String ticket) {
		return this.changeStatusRepository.findByTicketIdOrderByDateChangeStatusDesc(ticket);
	}

	@Override
	public Page<Ticket> findByCurrentUser(int page, int count, String user) {
		Pageable pages = new PageRequest(page, count);
		
		return this.ticketRepository.findByUserIdOrderByDateDesc(pages, user);
	}

	@Override
	public Page<Ticket> findByParameters(int page, int count, String title, String status, String priority) {
		Pageable pages = new PageRequest(page, count);
		return this.ticketRepository.findByTitleIgnoreCaseContainingAndStatusAndPriorityOrderByDateDesc(title, status, priority, pages);
	}

	@Override
	public Page<Ticket> findByParametersAndCurrentUser(int page, int count, String title, String status,
			String priority, String userId) {
		Pageable pages = new PageRequest(page, count);
		
		return this.ticketRepository.findByTitleIgnoreCaseContainingAndStatusAndPriorityAndUserIdOrderByDateDesc(title, status, priority, userId,pages);
	}

	@Override
	public Page<Ticket> findByNumber(int page, int count, Integer number) {
		Pageable pages = new PageRequest(page, count);
		
		return this.ticketRepository.findByNumber(number, pages);
	}

	@Override
	public Iterable<Ticket> findAll() {
		return this.ticketRepository.findAll();
	}

	@Override
	public Page<Ticket> findByParametersAndAssignedUser(int page, int count, String title, String status,
			String priority, String assignedUser) {
		Pageable pages = new PageRequest(page, count);
		
		return this.ticketRepository.findByTitleIgnoreCaseContainingAndStatusAndPriorityAndAssigneUserOrderByDateDesc(title, status, priority,assignedUser, pages);
	}

}
