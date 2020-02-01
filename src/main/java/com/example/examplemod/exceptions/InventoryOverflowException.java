package com.example.examplemod.exceptions;

public class InventoryOverflowException extends Exception
{
	public InventoryOverflowException(String message)
	{
		super(message);
	}
}
